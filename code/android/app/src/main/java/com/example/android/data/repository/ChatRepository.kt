package com.example.android.data.repository

import com.example.android.data.dto.ConversationDto
import com.example.android.data.dto.MessageDto
import com.example.android.data.remote.ChatService
import javax.inject.Inject

interface ChatRepository {
    suspend fun getConversations(): Result<List<ConversationDto>>
    suspend fun getHistory(otherUserId: Int, page: Int = 1): Result<List<MessageDto>>
}

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatService
) : ChatRepository {

    override suspend fun getConversations(): Result<List<ConversationDto>> =
        runCatching { api.getConversations() }

    override suspend fun getHistory(otherUserId: Int, page: Int): Result<List<MessageDto>> =
        runCatching { api.getHistory(otherUserId, page) }
}