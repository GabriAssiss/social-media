package com.example.android.data.repository

import ProfileResponse
import com.example.android.data.remote.UserService
import javax.inject.Inject

interface UserRepository {
    suspend fun myProfile() : Result<ProfileResponse>
}

class UserRepositoryImpl @Inject constructor(
    val api : UserService
) : UserRepository {
    override suspend fun myProfile(): Result<ProfileResponse> =
        runCatching { api.getMyProfile() }
}