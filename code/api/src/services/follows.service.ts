import type { FollowRequest } from "../dtos/FollowDTO.js";
import { BadRequestError } from '../utils/api-errors.js';
import userRepository from '../repositories/users.repository.js';
import followRepository from '../repositories/follows.repository.js';

class FollowService {

    async follow( data : FollowRequest) {
        const { followerId, followedId: followingId } = data;
        
        if(followerId === followingId) {
            throw new BadRequestError("A user cannot follow themselves.");
        }

        if(!await userRepository.findById(followerId)) {
            throw new BadRequestError("Invalid follower user ID.");
        }

        if(!await userRepository.findById(followingId)) {
            throw new BadRequestError("Invalid following user ID.");
        }

        if(await followRepository.isFollowed(data)) {
            throw new BadRequestError("You are already following this user.");
        }

        const follow = await followRepository.create(data);
    }

    async unfollow( data : FollowRequest) {
        const { followerId, followedId: followingId } = data;
        
        if(followerId === followingId) {
            throw new BadRequestError("A user cannot follow themselves.");
        }

        if(!await userRepository.findById(followerId)) {
            throw new BadRequestError("Invalid follower user ID.");
        }

        if(!await userRepository.findById(followingId)) {
            throw new BadRequestError("Invalid following user ID.");
        }

        if(!await followRepository.isFollowed(data)) {
            throw new BadRequestError("You are not following this user.");
        }

        const unfollow = await followRepository.delete(data);

    }

    async findAllFollowed(userId: number) {
        const follows = await followRepository.findAllFollowed(userId);
        return follows;
    }

    async findAllFollowers(userId: number) {
        const follows = await followRepository.findAllFollowers(userId);
        return follows;
    }
}

export default new FollowService();