package com.example.android.domain.repository

interface FollowRepository {
    suspend fun follow(id: Int, followedId: Int): Result<String>
}