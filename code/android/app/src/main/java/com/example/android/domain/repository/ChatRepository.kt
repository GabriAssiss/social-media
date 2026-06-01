package com.example.android.domain.repository

import com.example.android.data.dto.ConversationDto
import com.example.android.data.dto.MessageDto

interface ChatRepository {
    suspend fun getConversations(): Result<List<ConversationDto>>
    suspend fun getHistory(otherUserId: Int, page: Int = 1): Result<List<MessageDto>>
}