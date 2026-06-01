package com.example.android.data.remote.auth

import CreateUserRequest
import CreateUserResponse
import LoginRequest
import LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/auth/")
    suspend fun create(@Body body: CreateUserRequest) : CreateUserResponse

    @POST("/api/v1/auth/login")
    suspend fun login(@Body body: LoginRequest) : LoginResponse

    @PATCH("/api/v1/auth/fcm-token")
    suspend fun updateFcmToken(@Body body: Map<String, String>): Response<Unit>
}