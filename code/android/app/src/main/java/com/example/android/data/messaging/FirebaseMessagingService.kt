package com.example.android.data.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // Envia o token atualizado para sua API Express
        // Isso é chamado no primeiro acesso e quando o FCM rotaciona o token


    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title  = remoteMessage.notification?.title ?: return
        val body   = remoteMessage.notification?.body  ?: return
        val chatId = remoteMessage.data["chatId"]

        showNotification(title, body, chatId)
    }

    private fun showNotification(title: String, body: String, chatId: String?) {

    }
}