package com.example.android.data.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.android.MainActivity
import com.example.android.domain.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MessagingEntryPoint {
    fun authRepository(): AuthRepository
}

class FirebaseMessagingService : FirebaseMessagingService() {

    private val repository: AuthRepository by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            MessagingEntryPoint::class.java
        ).authRepository()
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Token atualizado: $token")
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateFcmToken(token)
                .onFailure { Log.e("FCM", "Erro ao enviar token: ${it.message}") }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title  = remoteMessage.notification?.title ?: return
        val body   = remoteMessage.notification?.body  ?: return
        val receiverId = remoteMessage.data["receiverId"]

        showNotification(title, body, receiverId)
    }

    private fun showNotification(title: String, body: String, receiverId: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                Log.w("FCM", "POST_NOTIFICATIONS permission not granted")
                return
            }
        }

        val channelId = "chat_messages"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(channelId, "Mensagens", NotificationManager.IMPORTANCE_HIGH)
            )
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                receiverId?.let { putExtra("receiverId", it) }
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        manager.notify(
            System.currentTimeMillis().toInt(),
            NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        )
    }
}