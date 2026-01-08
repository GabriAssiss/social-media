package com.example.android.ui.components

import ProfileImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.R

@Composable
fun UserCard(modifier: Modifier = Modifier) {
    val nickname = "Nickname"
    Surface(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp),
        ) {
        Row(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            ProfileImage(
                image = painterResource(id = R.drawable.default_avatar_icon),
                modifier = Modifier
                    .size(100.dp)
            )
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                ) {
                Text(
                    text = nickname,
                    fontSize = 16.sp
                )
                Spacer(modifier = modifier.padding(bottom = 12.dp))
                Text(
                    text ="Algo que foi escrito...",
                    fontSize = 16.sp
                    )
            }
        }
    }
}