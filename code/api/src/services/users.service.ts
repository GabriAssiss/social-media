import type { CreateUserRequest } from '../dtos/request/CreateUserRequest.js';
import UserRepository from '../repositories/users.repository.js';

class UsersService {

    async create(userData: CreateUserRequest) {
        const newUser = await UserRepository.create(userData);
        return newUser; 
    }
}

export default new UsersService();