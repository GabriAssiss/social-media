import type { Request, Response } from 'express';
import userService from '../services/users.service.js';

class UserController {

    async myProfile(req: Request, res: Response) {
        const userId = req.user.id;
        const response = await userService.profile(userId);
        return res.status(200).json({ user: response });
    }

    async profile(req: Request, res: Response) {
        const userId = Number(req.params.userId);
        const response = await userService.profile(userId);
        return res.status(200).json({ user: response });
    }

}

export default new UserController();