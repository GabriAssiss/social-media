package com.example.android.data.repository

import ProfileResponse
import com.example.android.data.dto.ConversationUserDto
import com.example.android.data.remote.user.UserService
import com.example.android.domain.repository.UserRepository
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    val api : UserService
) : UserRepository {
    override suspend fun myProfile(): Result<ProfileResponse> =
        runCatching { api.getMyProfile() }
    override suspend fun searchFollowersAndFollowing(query: String): Result<List<ConversationUserDto>> =
        runCatching { api.searchConnections(query) }
}