package com.example.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = ""
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateName(value: String) = _uiState.update { it.copy(name = value) }
    fun updateEmail(value: String) = _uiState.update { it.copy(email = value) }
    fun updatePhone(value: String) = _uiState.update { it.copy(phone = value) }
    fun updatePassword(value: String) = _uiState.update { it.copy(password = value) }

    fun register() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.create(
                state.name,
                state.email,
                state.password,
                state.phone
            ).onSuccess { response ->
                _uiState.update {
                    it.copy(isLoading = false, success = true)
                }
            }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Erro desconhecido")
                    }
                }
        }
    }

}
