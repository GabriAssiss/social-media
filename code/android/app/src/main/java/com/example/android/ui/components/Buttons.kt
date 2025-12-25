package com.example.android.ui.components


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AppOutlinedButton(onClick: () -> Unit, label: String) {
    Surface () {
        OutlinedButton(
            onClick = { onClick() },
            modifier = Modifier.width(300.dp).padding(bottom = 50.dp)
        ) {
            Text(label)
        }
    }
}