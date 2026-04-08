package com.example.android.data.remote

import com.example.android.data.dto.FollowDTO
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowService {

    @POST("/api/v1/follows/{id}")
    suspend fun follow(@Path("id") id: String, @Body body: FollowDTO): String

}