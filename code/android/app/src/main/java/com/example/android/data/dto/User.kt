data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val user: User,
    val token: String
)

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)

data class CreateUserResponse(
    val user: User,
    val password: String,
)

data class ProfileResponse(
    val id: Int,
    val name: String,
    val followersCount: Int,
    val followedCount: Int,
)