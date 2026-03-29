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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.R

data class Message(
    val text: String,
    val isFromMe: Boolean
)

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val messages = listOf(
        Message("Olá! Como você está?", isFromMe = false),
        Message("Tudo bem, e você? Viu o novo projeto?", isFromMe = true),
        Message("Sim! Ficou excelente. Acho que podemos avançar.", isFromMe = false),
        Message("Ótimo!", isFromMe = true),
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { ChatTextField() }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message = message)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val backgroundColor = if (message.isFromMe)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant

    val textColor = MaterialTheme.colorScheme.onSurface // ← sem hardcode
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
    ChatView()
}