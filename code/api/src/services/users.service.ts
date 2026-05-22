import type { ProfileResponse } from "../dtos/UserDTO.js";
import followsRepository from "../repositories/follows.repository.js";
import usersRepository from "../repositories/users.repository.js";
import { NotFoundError } from "../utils/api-errors.js";
import { Prisma } from '../../generated/prisma/client.js'
import prisma from '../database/prisma.client.js';

type SuggestedUser = { id: number; name: string; profileUrl: string | null; isRecommended: boolean };

class UserService {

    async profile(userId: number) {
        const user = await usersRepository.findById(userId);
        if (!user) {
            throw new NotFoundError("User not found.");
        }
        
        const followersCount = await followsRepository.followersCount(userId);
        const followingCount = await followsRepository.followedCount(userId);

        const response : ProfileResponse = {
            id: user.id,
            name: user.name,
            followersCount,
            followedCount: followingCount
        };

        return response;
    }

    async searchConnections(userId: number, q?: string) {
        const followed = await followsRepository.findAllFollowed(userId);
        const followers = await followsRepository.findAllFollowers(userId);

        const followedIds = followed.map(f => f.followedId);
        const followerIds = followers.map(f => f.followerId);

        const recommendedSet = new Set<number>([...followedIds, ...followerIds].filter(id => id !== userId));
        const recommendedIds = Array.from(recommendedSet);

        const nameFilter = q && q.trim() ? { name: { contains: q, mode: 'insensitive' as Prisma.QueryMode } } : undefined;

        const recommendedUsers = recommendedIds.length > 0 ? await prisma.user.findMany({
            where: Object.assign({ id: { in: recommendedIds } }, nameFilter ? { AND: nameFilter } : {}),
            select: { id: true, name: true, profile_url: true }
        }) : [];

        const notInIds = [userId, ...recommendedIds];
        const otherUsers = await prisma.user.findMany({
            where: Object.assign({ id: { notIn: notInIds } }, nameFilter ? { AND: nameFilter } : {}),
            select: { id: true, name: true, profile_url: true }
        });

        const recommendedMapped: SuggestedUser[] = recommendedUsers.map(u => ({ id: u.id, name: u.name, profileUrl: u.profile_url ?? null, isRecommended: true }));
        const otherMapped: SuggestedUser[] = otherUsers.map(u => ({ id: u.id, name: u.name, profileUrl: u.profile_url ?? null, isRecommended: false }));

        return [...recommendedMapped, ...otherMapped];
    }

}

export default new UserService();