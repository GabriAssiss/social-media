import type { Request, Response } from 'express';
import FollowService from '../services/users.service.js';

class FollowController {

    async follow(req: Request, res: Response) {
        const followerId = Number(req.params.followerId);
        const { followingId } = req.body;
        const response = await FollowService.follow({ followerId, followingId: Number(followingId) });
    }

}

export default new FollowController();