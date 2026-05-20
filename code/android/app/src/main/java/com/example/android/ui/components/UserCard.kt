package com.example.android.ui.components

import ProfileImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.android.R
import com.example.android.data.dto.ConversationDto

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    conversation: ConversationDto,
    onChatClick: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onChatClick(conversation.user.id) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val avatarPainter = if (!conversation.user.profileUrl.isNullOrEmpty()) {
                rememberAsyncImagePainter(conversation.user.profileUrl)
            } else {
                painterResource(id = R.drawable.default_avatar_icon)
            }

            ProfileImage(
                image = avatarPainter,
                modifier = Modifier.size(72.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = conversation.user.name,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conversation.lastMessage.text,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserCardPreview() {
    val fake = ConversationDto(
        user = com.example.android.data.dto.ConversationUserDto(
            id = 1,
            name = "Maria Silva",
            profileUrl = null
        ),
        lastMessage = com.example.android.data.dto.MessageDto(
            id = "1",
            text = "Oi, tudo bem com você?",
            senderId = 2,
            isFromMe = false
        )
    )
    UserCard(conversation = fake, onChatClick = {})
}