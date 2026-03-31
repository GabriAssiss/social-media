import {  Prisma } from '../../generated/prisma/client.js'
import prisma from '../database/prisma.client.js';
import type { FollowRequest, UnfollowRequest } from '../dtos/FollowDTO.js';

class FollowRepository {

    async isFollowed(data : FollowRequest) {
        const follow = await prisma.follow.findUnique({
            where: {
                followId: {
                    followerId: data.followerId,
                    followedId: data.followedId
                }
            }
        })

        return follow;
    }

    async create(data : FollowRequest) {
        const follow: Prisma.FollowCreateInput = {
            follower: { connect: { id: data.followerId } },
            followed: { connect: { id: data.followedId } }
        };

        const newFollow = await prisma.follow.create({
            data: follow
        });

        return newFollow;
    }

    async delete(data : UnfollowRequest) {
        const follow = await prisma.follow.delete({
            where: {
                followId: {
                    followerId: data.followerId,
                    followedId: data.followedId
                }
            }
        });

        return follow;
    }

    async findAllFollowed(userId: number) {
        const follows = await prisma.follow.findMany({
            where: {
                followerId: userId,
            },
        });
        return follows;
    }

    async findAllFollowers(userId: number) {
        const follows = await prisma.follow.findMany({
            where: {
                followedId: userId,
            },
        });
        return follows;
    }

    async followersCount(userId: number) {
        const count = await prisma.follow.count({
            where: {
                followedId: userId,
            },
        });
        return count;
    }

    async followedCount(userId: number) {
        const count = await prisma.follow.count({
            where: {
                followerId: userId,
            },
        });
        return count;
    }
}

export default new FollowRepository();