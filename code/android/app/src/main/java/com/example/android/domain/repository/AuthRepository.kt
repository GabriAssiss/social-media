package com.example.android.domain.repository

import CreateUserResponse
import LoginResponse

interface AuthRepository {
    suspend fun create(name: String, email: String, password: String, phone: String): Result<CreateUserResponse>
    suspend fun login(email: String, password: String): Result<LoginResponse>
}