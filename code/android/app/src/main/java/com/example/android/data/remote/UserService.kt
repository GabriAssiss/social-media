package com.example.android.data.remote

import CreateUserRequest
import CreateUserResponse
import LoginRequest
import LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/api/v1/user/")
    suspend fun create(@Body body: CreateUserRequest) : CreateUserResponse

    @POST("/api/v1/user/login")
    suspend fun login(@Body body: LoginRequest) : LoginResponse
}