package com.example.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.dto.MessageDto
import com.example.android.data.remote.SocketManager
import com.example.android.data.repository.ChatRepository
import com.example.android.di.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

data class ChatUiState(
    val messages: List<MessageDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val tokenManager: TokenManager,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var isListening = false
    private var myUserId: Int? = null
    private var currentPage = 1
    private var currentOtherUserId: Int? = null

    fun init(withUserId: Int) {
        myUserId = tokenManager.getId()
        currentOtherUserId = withUserId
        socketManager.connect()
        loadHistory(withUserId)
        listenSocket()
    }

    fun loadMore() {
        currentOtherUserId?.let {
            currentPage++
            loadHistory(it, currentPage)
        }
    }

    private fun loadHistory(otherUserId: Int, page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            chatRepository.getHistory(otherUserId, page)
                .onSuccess { messages ->
                    _uiState.update { currentState ->
                        val merged = (messages + currentState.messages)
                            .distinctBy { it.id }
                            .sortedBy { it.createdAt }
                        currentState.copy(messages = merged, isLoading = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    private fun listenSocket() {

        if (isListening) return
        isListening = true

        socketManager.onNewMessage { json ->
            viewModelScope.launch(Dispatchers.Main) {
                val message = parseMessage(json)
                _uiState.update { it.copy(messages = it.messages + message) }
            }
        }

        socketManager.onMessageSent { json ->
            viewModelScope.launch(Dispatchers.Main) {
                val message = parseMessage(json)
                _uiState.update { it.copy(messages = it.messages + message) }
            }
        }
    }

    fun sendMessage(receiverId: Int, text: String) {
        if (text.isBlank()) return
        socketManager.sendMessage(receiverId, text)
    }

    override fun onCleared() {
        isListening = false
        // socketManager.disconnect() // Dependendo da arquitetura, desconectar aqui pode matar a conexão de outras telas. 
        // Em apps modernos o socket costuma seguir o ciclo de vida do usuario autenticado, ou usar eventos de lifecycle para pause/resume da tela.
        super.onCleared()
    }

    private fun parseMessage(json: JSONObject) = MessageDto(
        id = json.getString("_id"),
        text = json.optString("text"),
        senderId = json.getInt("senderId"),
        isFromMe = json.getInt("senderId") == myUserId,
        createdAt = json.optString("createdAt").ifBlank { null }
    )
}