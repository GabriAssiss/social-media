package com.example.android.data.remote

import com.example.android.data.dto.ConversationDto
import com.example.android.data.dto.MessageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatService {

    @GET("/api/v1/chat/conversations")
    suspend fun getConversations(): List<ConversationDto>

    @GET("/api/v1/chat/history/{otherUserId}")
    suspend fun getHistory(
        @Path("otherUserId") otherUserId: Int,
        @Query("page") page: Int = 1
    ): List<MessageDto>
}