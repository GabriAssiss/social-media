import usersRepository from "../repositories/users.repository.js";

class UserService {
    async findAll() {
        return await usersRepository.findAll();
    }
}

export default new UserService();