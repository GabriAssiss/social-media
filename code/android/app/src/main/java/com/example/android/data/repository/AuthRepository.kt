package com.example.android.data.repository

import CreateUserRequest
import CreateUserResponse
import LoginRequest
import LoginResponse
import com.example.android.data.remote.auth.AuthService
import com.example.android.domain.repository.AuthRepository
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService
) : AuthRepository {

    override suspend fun create(name: String, email: String, password: String, phone: String): Result<CreateUserResponse> =
        runCatching { api.create(CreateUserRequest(name, email, password, phone)) }

    override suspend fun login(email: String, password: String): Result<LoginResponse> =
        runCatching { api.login(LoginRequest(email, password)) }

    override suspend fun updateFcmToken(token: String): Result<Unit> =
        runCatching { api.updateFcmToken(mapOf("fcmToken" to token)) }
}