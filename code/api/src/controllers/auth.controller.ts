import type { Request, Response } from 'express';
import { BadRequestError } from '../utils/api-errors.js';
import UserService from '../services/auth.service.js';

class AuthController {

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
        return res.status(201).json({ user: {
                id: newUser.id,
                name: newUser.name,
                email: newUser.email,
                phone: newUser.phone,
                password: newUser.password
            } });
    }

    async login(req: Request, res: Response) {
        const { email, password } = req.body;
        
        if(!email) {
            throw new BadRequestError('Email is required.');
        }
        if(!password) {
            throw new BadRequestError('Password is required.');
        }

        const response = await UserService.login({ email, password });

        return res.status(200).json({ 
            user: {
                id: response.id,
                name: response.name,
                email: response.email,
                phone: response.phone,
            },
            token: response.token
         });
    }

}

export default new AuthController();