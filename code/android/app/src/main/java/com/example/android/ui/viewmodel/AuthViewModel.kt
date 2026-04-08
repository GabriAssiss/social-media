package com.example.android.ui.viewmodel

import User
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.repository.AuthRepository
import com.example.android.di.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val token: String? = null,
    val user: User? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        val token = tokenManager.getToken()
        if (token != null) {
            _uiState.update { it.copy(token = token) }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            repository.login(email, password)
                .onSuccess { response ->
                    tokenManager.saveToken(response.token)
                    _uiState.update {
                        it.copy(isLoading = false, user = response.user, token = response.token)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Erro desconhecido")
                    }
                }
        }
    }

    fun logout() {
        tokenManager.clearToken()
        _uiState.update { AuthUiState() }
    }
}

