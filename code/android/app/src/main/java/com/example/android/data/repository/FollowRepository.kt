package com.example.android.data.repository

import ProfileResponse
import com.example.android.data.dto.FollowDTO
import com.example.android.data.remote.FollowService
import javax.inject.Inject

interface FollowRepository {
    suspend fun follow(id: Int, followedId: Int): Result<String>
}

class FollowRepositoryImpl @Inject constructor(
    private val followService: FollowService
) : FollowRepository {

    override suspend fun follow(
        id: Int,
        followedId: Int
    ): Result<String> {
        val body = FollowDTO(userId = followedId)
        return runCatching { followService.follow(id = id.toString(), body = body) }
    }

}