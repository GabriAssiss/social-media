package com.example.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.data.repository.FollowRepository
import com.example.android.data.repository.UserRepository
import com.example.android.di.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String? = null,
    val error: String? = null,
    val followersCount: Int = 0,
    val followedCount: Int = 0,
    val isFollowed: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {


    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        getProfile(
            userId = tokenManager.getId()
        )
    }


    fun getProfile(userId: Int?) {
        viewModelScope.launch {
            userRepository.myProfile()

                .onSuccess { response ->
                    println("DEBUG: $response")
                    _uiState.update {
                        it.copy(name = response.name, followedCount = response.followedCount, followersCount = response.followersCount)
                    }
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message)
                }
        }
    }

    fun follow(userId: Int, followedId: Int) {
        viewModelScope.launch {
            followRepository.follow(id = userId, followedId = followedId)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isFollowed = true,
                        )
                    }
                }
                .onFailure { _uiState.value = _uiState.value.copy(error = it.message) }
        }
    }


}