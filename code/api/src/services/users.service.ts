import type { FollowRequest } from "../dtos/request/FollowRequest.js";
import { BadRequestError } from '../utils/api-errors.js';
import UserRepository from '../repositories/users.repository.js';
import FollowRepository from '../repositories/follows.repository.js';

class FollowService {

    async follow( data : FollowRequest) {
        const { followerId, followingId } = data;
        
        if(followerId === followingId) {
            throw new BadRequestError("A user cannot follow themselves.");
        }

        if(!UserRepository.findById(followerId)) {
            throw new BadRequestError("Invalid follower user ID.");
        }

        if(!UserRepository.findById(followingId)) {
            throw new BadRequestError("Invalid following user ID.");
        }

        if(await FollowRepository.isFollowed(data)) {
            throw new BadRequestError("You are already following this user.");
        }

        const follow = await FollowRepository.create(data);
    }
}

export default new FollowService();