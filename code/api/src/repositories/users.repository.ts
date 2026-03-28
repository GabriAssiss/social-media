import type { CreateUserRequest } from '../dtos/UserDTO.js'; 
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

    async findByEmail(email: string) {
        const user = await prisma.user.findUnique({
            where: {
                email: email
            }
        });

        return user;
    }

    async findByPhone(phone: string) {
        const user = await prisma.user.findUnique({
            where: {
                phone: phone
            }
        });

        return user;
    }

    async findAll() {
        const users = await prisma.user.findMany();
        return users;
    }

    async findById(id: number) {
        const user = await prisma.user.findUnique({
            where: {
                id: id
            }
        });

        return user;
    }

    async deleteById(id: number) {
        await prisma.user.delete({
            where: {
                id: id
            }
        });
    }

}

export default new UserRepository();