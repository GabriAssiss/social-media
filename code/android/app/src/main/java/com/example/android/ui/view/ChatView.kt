package com.example.android.ui.view

import ChatTextField
import ProfileImage
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime
import java.time.ZoneId
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.android.R
import com.example.android.data.dto.MessageDto
import com.example.android.ui.viewmodel.ChatUiState

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    withUserId: Int = 0,
    uiState: ChatUiState,
    inputText: String = "",
    onTextChange: (String) -> Unit = {},
    onSendMessage: (Int, String) -> Unit = { _, _ -> },
    onInit: (Int) -> Unit = {},
    onLoadMore: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val listState = rememberLazyListState()

    LaunchedEffect(withUserId) {
        onInit(withUserId)
    }

    var previousSize by remember { mutableIntStateOf(0) }
    LaunchedEffect(uiState.messages.size) {
        val newSize = uiState.messages.size
        if (newSize > previousSize && (previousSize == 0 || !listState.canScrollForward)) {
            if (newSize > 0) {
                listState.animateScrollToItem(newSize - 1)
            }
        }
        previousSize = newSize
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { firstVisibleIndex ->
                if (firstVisibleIndex == 0 && uiState.messages.isNotEmpty()) {
                    onLoadMore()
                }
            }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            ChatTextField(
                value = inputText,
                onValueChange = onTextChange,
                onSendClick = {
                    onSendMessage(withUserId, inputText)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatBubble(message: MessageDto) {
    val backgroundColor = if (message.isFromMe)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant

    val textColor = if (message.isFromMe)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurfaceVariant
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
                    image = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://ui-avatars.com/api/?name=User+${message.senderId}&background=random")
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.default_avatar_icon),
                        error = painterResource(id = R.drawable.default_avatar_icon)
                    ),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(horizontalAlignment = alignment) {
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
                
                message.createdAt?.let { timeString ->
                    Text(
                        text = try {
                            val zdt = ZonedDateTime.parse(timeString)
                            zdt.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"))
                        } catch (e: Exception) {
                            timeString.take(5)
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatViewPreview() {
    ChatView(uiState = ChatUiState())
}