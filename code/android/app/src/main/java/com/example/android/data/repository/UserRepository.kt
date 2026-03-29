package com.example.android.data.repository

import CreateUserRequest
import CreateUserResponse
import LoginRequest
import LoginResponse
import com.example.android.data.remote.AuthService
import javax.inject.Inject

interface UserRepository {
    suspend fun create(name: String, email: String, password: String, phone: String) : Result<CreateUserResponse>
    suspend fun login(email: String, password: String): Result<LoginResponse>
}

class UserRepositoryImpl @Inject constructor(
    private val api: AuthService
) : UserRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> =
        runCatching { api.login(LoginRequest(email, password)) }

    override suspend fun create(name: String, email: String, password: String, phone: String): Result<CreateUserResponse> =
        runCatching { api.create(CreateUserRequest(name, email, password, phone)) }
}