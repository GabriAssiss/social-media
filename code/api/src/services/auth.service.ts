import type { CreateUserRequest } from '../dtos/request/CreateUserRequest.js';
import type { LoginUserRequest } from '../dtos/request/LoginUserRequest.js';
import UserRepository from '../repositories/users.repository.js';
import bcrypt from 'bcrypt';
import jwt, { type SignOptions } from 'jsonwebtoken';
import { NotFoundError, UnauthorizedError } from '../utils/api-errors.js';

class AuthService {

    async create(userData: CreateUserRequest) {
        userData.password = await bcrypt.hash(userData.password, 10);
        const newUser = await UserRepository.create(userData);
        return newUser; 
    }

    async login(userData: LoginUserRequest) {
        const user = await UserRepository.findByEmail(userData.email);

        if(!user) {
            throw new NotFoundError('User not found.');
        }

        const isPasswordValid = await bcrypt.compare(userData.password, user.password);
        if(!isPasswordValid) {
            throw new UnauthorizedError('Invalid password.');
        }

        const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET ?? "", { expiresIn: process.env.JWT_EXPIRES_IN ?? "1h"} as SignOptions);
        const response = { ...user, token};
        return response;
    }


}

export default new AuthService();