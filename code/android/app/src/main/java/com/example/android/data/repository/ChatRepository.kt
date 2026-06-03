package com.example.android.data.repository

import com.example.android.data.dto.ConversationDto
import com.example.android.data.dto.MessageDto
import com.example.android.data.remote.chat.ChatService
import com.example.android.di.TokenManager
import com.example.android.domain.repository.ChatRepository
import javax.inject.Inject


class ChatRepositoryImpl @Inject constructor(
    private val api: ChatService,
    private val tokenManager: TokenManager
) : ChatRepository {

    override suspend fun getConversations(): Result<List<ConversationDto>> =
        runCatching { api.getConversations() }

    override suspend fun getHistory(otherUserId: Int, page: Int): Result<List<MessageDto>> =
        runCatching {
            val myId = tokenManager.getId()
            api.getHistory(otherUserId, page).map { message ->
                message.copy(isFromMe = message.senderId == myId)
            } }
}