package com.example.android.data.repository

import ProfileResponse
import com.example.android.data.dto.FollowDTO
import com.example.android.data.remote.follow.FollowService
import com.example.android.domain.repository.FollowRepository
import javax.inject.Inject


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