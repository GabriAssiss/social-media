import type { Request, Response } from 'express';
import userService from '../services/users.service.js';

class UserController {

    async myProfile(req: Request, res: Response) {
        const userId = req.user.id;
        const response = await userService.profile(userId);
        return res.status(200).json(response);
    }

    async profile(req: Request, res: Response) {
        const userId = Number(req.params.userId);
        const response = await userService.profile(userId);
        return res.status(200).json(response);
    }

    async searchConnections(req: Request, res: Response) {
        const userId = req.user.id;
        const q = (req.query.q as string) || '';
        const results = await userService.searchConnections(userId, q);
        return res.status(200).json(results);
    }

}

export default new UserController();