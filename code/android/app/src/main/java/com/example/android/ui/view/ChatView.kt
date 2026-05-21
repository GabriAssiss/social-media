package com.example.android.ui.view

import ChatTextField
import ProfileImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.R
import com.example.android.data.dto.MessageDto
import com.example.android.ui.viewmodel.ChatUiState

@Composable
fun ChatView(
    withUserId: Int = 0,
    uiState: ChatUiState,
    onSendMessage: (Int, String) -> Unit = { _, _ -> },
    onInit: (Int) -> Unit = {},
    onLoadMore: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    LaunchedEffect(withUserId) {
        onInit(withUserId)
    }

    val previousSize = remember { mutableStateOf(0) }
    LaunchedEffect(uiState.messages.size) {
        val newSize = uiState.messages.size
        if (newSize > previousSize.value && (previousSize.value == 0 || !listState.canScrollForward)) {
            if (newSize > 0) {
                listState.animateScrollToItem(newSize - 1)
            }
        }
        previousSize.value = newSize
    }

    LaunchedEffect(listState.canScrollBackward) {
        if (!listState.canScrollBackward && uiState.messages.isNotEmpty()) {
            onLoadMore()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            ChatTextField(
                value = text,
                onValueChange = { text = it },
                onSendClick = {
                    onSendMessage(withUserId, text)
                    text = ""
                }
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.messages, key = { it.id }) { message ->
                    ChatBubble(message = message)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: MessageDto) {
    val backgroundColor = if (message.isFromMe)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant

    val textColor = MaterialTheme.colorScheme.onSurface
    val alignment = if (message.isFromMe) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start
        ) {
            if (!message.isFromMe) {
                ProfileImage(
                    image = painterResource(id = R.drawable.default_avatar_icon),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Surface(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 2.dp,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(12.dp),
                    color = textColor
                )
            }
        }
    }
}

@Preview
@Composable
fun ChatViewPreview() {
    ChatView(uiState = ChatUiState())
}