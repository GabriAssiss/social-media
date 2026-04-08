package com.example.android.data.repository

import CreateUserRequest
import CreateUserResponse
import LoginRequest
import LoginResponse
import com.example.android.data.remote.AuthService
import javax.inject.Inject

interface AuthRepository {
    suspend fun create(name: String, email: String, password: String, phone: String) : Result<CreateUserResponse>
    suspend fun login(email: String, password: String): Result<LoginResponse>

}

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService
) : AuthRepository {

    override suspend fun create(name: String, email: String, password: String, phone: String): Result<CreateUserResponse> =
        runCatching { api.create(CreateUserRequest(name, email, password, phone)) }

    override suspend fun login(email: String, password: String): Result<LoginResponse> =
        runCatching { api.login(LoginRequest(email, password)) }


}