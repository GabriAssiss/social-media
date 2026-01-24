import type { CreateUserRequest } from '../dtos/request/CreateUserRequest.js'; 
import {  Prisma } from '../../generated/prisma/client.js'
import prisma from '../database/prisma.client.js';

class UserRepository {
    async create(userData: CreateUserRequest) {

        const user: Prisma.UserCreateInput = {
            name: userData.name,
            email: userData.email,
            password: userData.password,
            phone: userData.phone,
        };

        const newUser = await prisma.user.create({
            data: user
        });

        return newUser;
    }
}

export default new UserRepository();