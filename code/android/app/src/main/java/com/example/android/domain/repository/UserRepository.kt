package com.example.android.domain.repository

import ProfileResponse
import com.example.android.data.dto.ConversationUserDto

interface UserRepository {
    suspend fun myProfile(): Result<ProfileResponse>
    suspend fun searchFollowersAndFollowing(query: String): Result<List<ConversationUserDto>>
}