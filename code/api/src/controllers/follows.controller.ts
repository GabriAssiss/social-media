import type { Request, Response } from 'express';
import FollowService from '../services/follows.service.js';

class FollowController {

    async follow(req: Request, res: Response) {
        const followerId = Number(req.params.followerId);
        const { followingId } = req.body;
        const response = await FollowService.follow({ followerId, followedId: Number(followingId) });
        return res.status(201).json({ message: "Followed successfully." });
    }

    async unfollow(req: Request, res: Response) {
        const followerId = Number(req.params.followerId);
        const { followingId } = req.body;
        const response = await FollowService.unfollow({ followerId, followedId: Number(followingId) });
        return res.status(204).json({ message: "Unfollowed successfully." });
    }

    async findAllFollowed(req: Request, res: Response) {
        const userId = Number(req.params.followerId);
        const response = await FollowService.findAllFollowed(userId);
        return res.status(200).json({ follows: response });
    }

    async findAllFollowers(req: Request, res: Response) {
        const userId = Number(req.params.followerId);
        const response = await FollowService.findAllFollowers(userId);
        return res.status(200).json({ follows: response });
    }

}

export default new FollowController();