package com.example.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.dto.ConversationDto
import com.example.android.data.dto.ConversationUserDto
import com.example.android.domain.repository.ChatRepository
import com.example.android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.asStateFlow

sealed interface ConversationsUiState {
    data object Loading : ConversationsUiState

    @Immutable
    data class Success(val conversations: List<ConversationDto>) : ConversationsUiState

    @Immutable
    data class Error(val message: String) : ConversationsUiState
}

sealed interface UserSearchUiState {
    data object Idle : UserSearchUiState
    data object Loading : UserSearchUiState

    @Immutable
    data class Success(val users: List<ConversationUserDto>) : UserSearchUiState

    @Immutable
    data class Error(val message: String) : UserSearchUiState
}

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _conversationsState = MutableStateFlow<ConversationsUiState>(ConversationsUiState.Loading)
    val conversationsState = _conversationsState.asStateFlow()

    private val _searchState = MutableStateFlow<UserSearchUiState>(UserSearchUiState.Idle)
    val searchState = _searchState.asStateFlow()

    val searchQuery = MutableStateFlow("")

    init {
        loadConversations()
        observeSearchQuery()
    }

    fun loadConversations() {
        viewModelScope.launch {
            _conversationsState.update { ConversationsUiState.Loading }
            chatRepository.getConversations()
                .onSuccess { list ->
                    _conversationsState.update { ConversationsUiState.Success(list) }
                }
                .onFailure { e ->
                    _conversationsState.update { ConversationsUiState.Error(e.message ?: "Erro desconhecido") }
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                searchUsers(query)
            }
            .launchIn(viewModelScope)
    }

    private fun searchUsers(query: String) {
        viewModelScope.launch {
            _searchState.update { UserSearchUiState.Loading }
            userRepository.searchFollowersAndFollowing(query)
                .onSuccess { users ->
                    _searchState.update { UserSearchUiState.Success(users) }
                }
                .onFailure { e ->
                    _searchState.update { UserSearchUiState.Error(e.message ?: "Erro ao buscar usuários") }
                }
        }
    }
}