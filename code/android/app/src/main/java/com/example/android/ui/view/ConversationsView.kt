package com.example.android.ui.view

import AppBottomNavigationBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.input.TextFieldState
import com.example.android.ui.components.AppTopNavigation
import com.example.android.ui.components.UserCard
import com.example.android.ui.viewmodel.ConversationsUiState
import com.example.android.ui.viewmodel.ConversationsViewModel
import com.example.android.ui.viewmodel.UserSearchUiState

@Composable
fun ConversationsView(
    modifier: Modifier = Modifier,
    viewModel: ConversationsViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {},
    onChatClick: (Int) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val conversationsState by viewModel.conversationsState.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val textFieldState = rememberTextFieldState()

    ConversationsContent(
        modifier = modifier,
        conversationsState = conversationsState,
        searchState = searchState,
        textFieldState = textFieldState,
        onQueryChange = { query -> viewModel.searchQuery.value = query },
        onNavigate = onNavigate,
        onChatClick = onChatClick,
        onLogout = onLogout
    )
}

@Composable
fun ConversationsContent(
    modifier: Modifier = Modifier,
    conversationsState: ConversationsUiState,
    searchState: UserSearchUiState,
    textFieldState: TextFieldState,
    onQueryChange: (String) -> Unit,
    onNavigate: (String) -> Unit,
    onChatClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopNavigation(
                textFieldState = textFieldState,
                onQueryChange = onQueryChange,
                searchState = searchState,
                onUserSelected = { user ->
                    onChatClick(user.id)
                },
                onLogout = onLogout
            )
        },
        bottomBar = {
            AppBottomNavigationBar(
                currentRoute = "messages",
                onBottomNavigateClick = { route -> onNavigate(route) }
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val state = conversationsState) {
                is ConversationsUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ConversationsUiState.Error -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message)
                    }
                }

                is ConversationsUiState.Success -> {
                    if (state.conversations.isEmpty()) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Nenhuma conversa ainda.")
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            state.conversations.forEach { conversation ->
                                UserCard(
                                    modifier = modifier,
                                    conversation = conversation,
                                    onChatClick = onChatClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ConversationsViewPreview() {
    val textFieldState = rememberTextFieldState()
    ConversationsContent(
        conversationsState = ConversationsUiState.Success(emptyList()),
        searchState = UserSearchUiState.Idle,
        textFieldState = textFieldState,
        onQueryChange = {},
        onNavigate = {},
        onChatClick = {},
        onLogout = {}
    )
}