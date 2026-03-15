package com.example.android.ui.components


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AppOutlinedButton(
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true
) {
    Surface {
        OutlinedButton(
            onClick = { onClick() },
            modifier = Modifier.width(300.dp).padding(bottom = 50.dp),
            enabled = enabled
        ) {
            Text(label)
        }
    }
}

@Composable
fun AppReturnButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null
        )
    }
}