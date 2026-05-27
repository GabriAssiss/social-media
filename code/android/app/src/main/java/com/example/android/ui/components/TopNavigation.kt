package com.example.android.ui.components

import ProfileImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.snapshotFlow
import com.example.android.R
import com.example.android.data.dto.ConversationUserDto
import com.example.android.ui.viewmodel.UserSearchUiState

@Composable
private fun UserSearchResultItem(
    user: ConversationUserDto,
    textFieldState: TextFieldState,
    onUserSelected: (ConversationUserDto) -> Unit,
    onClose: () -> Unit
) {
    ListItem(
        leadingContent = {
            val painter = if (!user.profileUrl.isNullOrEmpty()) {
                rememberAsyncImagePainter(user.profileUrl)
            } else {
                painterResource(id = R.drawable.default_avatar_icon)
            }
            ProfileImage(
                image = painter,
                modifier = Modifier.size(40.dp)
            )
        },
        headlineContent = {
            Text(
                text = user.name,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = Modifier
            .clickable {
                textFieldState.edit { replace(0, length, user.name) }
                onClose()
                onUserSelected(user)
            }
            .fillMaxWidth()
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopNavigation(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    onQueryChange: (String) -> Unit,
    searchState: UserSearchUiState,
    showSearch: Boolean = true,
    onUserSelected: (ConversationUserDto) -> Unit,
    onLogout: () -> Unit = {}
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 2.dp,
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = { Text("") },
                navigationIcon = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.options))
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.logout)) },
                                onClick = {
                                    menuExpanded = false
                                    onLogout()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = stringResource(R.string.logout),
                                    )
                                }
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = stringResource(R.string.notifications))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            if (showSearch) {
                UserSearchBar(
                    textFieldState = textFieldState,
                    onQueryChange = onQueryChange,
                    searchState = searchState,
                    onUserSelected = onUserSelected,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSearchBar(
    textFieldState: TextFieldState,
    onQueryChange: (String) -> Unit,
    searchState: UserSearchUiState,
    onUserSelected: (ConversationUserDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var visibleCount by rememberSaveable { mutableIntStateOf(10) }

    Box(modifier.semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onExpandedChange = { expanded = it },
                    onQueryChange = { newValue ->
                        textFieldState.edit { replace(0, length, newValue) }
                        onQueryChange(newValue)
                        if (newValue.isBlank()) {
                            expanded = true
                        }
                    },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    placeholder = { Text(stringResource(R.string.search_placeholder)) }
                )
            },
            expanded = expanded,
            onExpandedChange = {
                expanded = it
                if (it && textFieldState.text.toString().isBlank()) {
                    onQueryChange("")
                }
            },
        ) {
            when (searchState) {
                is UserSearchUiState.Loading -> {
                    LaunchedEffect(searchState) {
                        visibleCount = 10
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }

                is UserSearchUiState.Success -> {
                    val onCloseState = rememberUpdatedState({ expanded = false })
                    LaunchedEffect(searchState.users) {
                        visibleCount = 10
                    }

                    val orderedUsers = remember(searchState.users) {
                        searchState.users
                            .filter { it.isRecommended }
                            .plus(searchState.users.filterNot { it.isRecommended })
                    }
                    val visibleUsers = remember(orderedUsers, visibleCount) {
                        orderedUsers.take(visibleCount.coerceAtMost(orderedUsers.size))
                    }
                    val searchRows = remember(visibleUsers) {
                        buildList {
                            val recommendedUsers = visibleUsers.filter { it.isRecommended }
                            val otherUsers = visibleUsers.filterNot { it.isRecommended }

                            if (recommendedUsers.isNotEmpty()) {
                                add("recommended-header")
                                addAll(recommendedUsers)
                            }
                            if (otherUsers.isNotEmpty()) {
                                add("other-header")
                                addAll(otherUsers)
                            }
                        }
                    }

                    LaunchedEffect(listState, searchRows.size, orderedUsers.size) {
                        snapshotFlow {
                            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                        }.collectLatest { lastVisibleIndex ->
                            if (searchRows.isNotEmpty() && lastVisibleIndex >= searchRows.size - 3) {
                                visibleCount = (visibleCount + 10).coerceAtMost(orderedUsers.size)
                            }
                        }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.heightIn(max = 360.dp)
                    ) {
                        items(
                            searchRows,
                            key = { item ->
                                when (item) {
                                    is String -> item
                                    is ConversationUserDto -> "user-${item.id}"
                                    else -> item.hashCode().toString()
                                }
                            },
                            contentType = { item ->
                                when (item) {
                                    is String -> "header"
                                    is ConversationUserDto -> item::class
                                    else -> null
                                }
                            }
                        ) { item ->
                            when (item) {
                                "recommended-header" -> Text(
                                    text = stringResource(R.string.recommended_label),
                                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
                                )

                                "other-header" -> Text(
                                    text = stringResource(R.string.other_profiles_label),
                                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
                                )

                                is ConversationUserDto -> UserSearchResultItem(
                                    user = item,
                                    textFieldState = textFieldState,
                                    onUserSelected = onUserSelected,
                                    onClose = onCloseState.value
                                )
                            }
                        }
                    }
                }

                is UserSearchUiState.Error -> {
                    Text(
                        text = searchState.message,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp
                    )
                }

                UserSearchUiState.Idle -> { /* nada */ }
            }
        }
    }
}