import {  Prisma } from '../../generated/prisma/client.js'
import prisma from '../database/prisma.client.js';
import type { FollowRequest } from '../dtos/request/FollowRequest.js';

class FollowRepository {

    async isFollowed(data : FollowRequest) {
        const follow = await prisma.follow.findUnique({
            where: {
                followerId_followingId: {
                    followerId: data.followerId,
                    followingId: data.followingId
                }
            }
        })

        return follow;
    }

    async create(data : FollowRequest) {
        const follow: Prisma.FollowCreateInput = {
            follower: { connect: { id: data.followerId } },
            following: { connect: { id: data.followingId } }
        };

        const newFollow = await prisma.follow.create({
            data: follow
        });

        return newFollow;
    }
}

export default new FollowRepository();