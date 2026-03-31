import type { ProfileResponse } from "../dtos/UserDTO.js";
import followsRepository from "../repositories/follows.repository.js";
import usersRepository from "../repositories/users.repository.js";
import { NotFoundError } from "../utils/api-errors.js";

class UserService {
    async profile(userId: number) {
        const user = await usersRepository.findById(userId);
        if (!user) {
            throw new NotFoundError("User not found.");
        }
        
        const followersCount = await followsRepository.followersCount(userId);
        const followingCount = await followsRepository.followedCount(userId);

        const response : ProfileResponse = {
            id: user.id,
            name: user.name,
            followersCount,
            followedCount: followingCount
        };

        return response;
    }
}

export default new UserService();