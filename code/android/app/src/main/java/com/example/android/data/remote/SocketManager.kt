package com.example.android.data.remote

import com.example.android.BuildConfig
import com.example.android.di.TokenManager
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(
    private val tokenManager: TokenManager
) {
    private var socket: Socket? = null

    fun connect() {

        if (socket?.connected() == true) return

        try {
            val options = IO.Options.builder()
                .setAuth(mapOf("token" to tokenManager.getToken()))
                .build()
            socket = IO.socket(BuildConfig.API_URL, options)
            socket?.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(receiverId: Int, messageText: String) {
        val data = JSONObject().apply {
            put("receiverId", receiverId)
            put("text", messageText)
        }
        socket?.emit("send_message", data)
    }

    fun getMessages(withUserId: Int, page: Int = 1) {
        val data = JSONObject().apply {
            put("withUserId", withUserId)
            put("page", page)
        }
        socket?.emit("get_messages", data)
    }

    fun markAsRead(senderId: Int) {
        val data = JSONObject().apply {
            put("senderId", senderId)
        }
        socket?.emit("read_messages", data)
    }


    fun onNewMessage(callback: (JSONObject) -> Unit) {
        socket?.on("new_message") { args ->
            if (args.isNotEmpty()) callback(args[0] as JSONObject)
        }
    }

    fun onMessageSent(callback: (JSONObject) -> Unit) {
        socket?.on("message_sent") { args ->
            if (args.isNotEmpty()) callback(args[0] as JSONObject)
        }
    }

    fun onMessagesHistory(callback: (JSONArray) -> Unit) {
        socket?.on("messages_history") { args ->
            if (args.isNotEmpty()) callback(args[0] as JSONArray)
        }
    }

    fun onMessagesRead(callback: (JSONObject) -> Unit) {
        socket?.on("messages_read") { args ->
            if (args.isNotEmpty()) callback(args[0] as JSONObject)
        }
    }

    fun disconnect() {
        socket?.off()
        socket?.disconnect()
        socket = null
    }
}

