import type { CreateUserRequest, LoginUserRequest } from '../dtos/UserDTO.js';
import UserRepository from '../repositories/users.repository.js';
import bcrypt from 'bcrypt';
import jwt, { type SignOptions } from 'jsonwebtoken';
import { NotFoundError, ConflictError, UnauthorizedError } from '../utils/api-errors.js';

class AuthService {

    async create(userData: CreateUserRequest) {

        if(await UserRepository.findByEmail(userData.email)) {
            throw new ConflictError('Email already in use.');
        }

        if(await UserRepository.findByPhone(userData.phone)) {
            throw new ConflictError('Phone already in use.');
        }

        if(await UserRepository.findByName(userData.name)) {
            throw new ConflictError('Name already in use.');
        }

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

        const jwtSecret = process.env.JWT_SECRET;
        if (!jwtSecret) {
            throw new Error('JWT_SECRET environment variable is not configured');
        }
        const expiresIn = process.env.JWT_EXPIRES_IN ?? '1h';
        const token = jwt.sign({ id: user.id }, jwtSecret, { expiresIn } as SignOptions);
        const response = { ...user, token};
        return response;
    }

    async updateFcmToken(userId: number, fcmToken: string) {
    await UserRepository.updateFcmToken(userId, fcmToken)
}


}

export default new AuthService();