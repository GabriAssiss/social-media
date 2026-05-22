package com.example.android.data.dto

data class MessageDto(
    val id: String,
    val text: String,
    val senderId: Int,
    val isFromMe: Boolean,
    val createdAt: String? = null
)


data class ConversationDto(
    val user: ConversationUserDto,
    val lastMessage: MessageDto
)

data class ConversationUserDto(
    val id: Int,
    val name: String,
    val profileUrl: String?,
    val isRecommended: Boolean = false
)