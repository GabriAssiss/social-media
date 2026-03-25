package com.example.android.data.remote

import CreateUserRequest
import CreateUserResponse
import LoginRequest
import LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/auth/")
    suspend fun create(@Body body: CreateUserRequest) : CreateUserResponse

    @POST("/api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest) : LoginResponse
}