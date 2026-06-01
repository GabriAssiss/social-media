package com.example.android.data.remote.user

import ProfileResponse
import com.example.android.data.dto.ConversationUserDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET("/api/v1/users/me/profile")
    suspend fun getMyProfile() : ProfileResponse

    @GET("/api/v1/users/connections/search")
    suspend fun searchConnections(
        @Query("q") query: String
    ): List<ConversationUserDto>
}