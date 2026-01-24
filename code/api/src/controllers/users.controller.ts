import type { Request, Response } from 'express';
import { BadRequestError } from '../utils/api-errors.js';
import UserService from '../services/users.service.js';

class UserController {

    async create(req: Request, res: Response) {
        const { name, email, password, phone } = req.body;
        
        if(!name) {
            throw new BadRequestError('Name is required.');
        }
        if(!password) {
            throw new BadRequestError('Password is required.');
        }
        if(!email || !phone) {
            throw new BadRequestError('Email or phone are required.');
        }

        const newUser = await UserService.create({ name, email, password, phone });
        return res.status(201).json(newUser);
    }
}

export default new UserController();