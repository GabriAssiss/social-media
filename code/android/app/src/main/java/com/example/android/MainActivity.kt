package com.example.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android.ui.nav.AppNavigation
import com.example.android.ui.theme.AndroidTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receiverId = intent.getStringExtra("receiverId")
        
        enableEdgeToEdge()
        setContent {
            AndroidTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AppNavigation(deepLinkReceiverId = receiverId)
                }
            }
        }
    }
}
