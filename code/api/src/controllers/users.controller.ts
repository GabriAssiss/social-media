import type { Request, Response } from 'express';
import userService from '../services/users.service.js';

class UserController {

    async findAll(req: Request, res: Response) {
        return res.status(200).json({ users: await userService.findAll() });
    }
}

export default new UserController();