package com.example.android.di

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) = prefs.edit { putString("jwt_token", token) }
    fun getToken(): String? = prefs.getString("jwt_token", null)
    fun clearToken() = prefs.edit { remove("jwt_token") }

    fun getId() : Int? {
        val token = getToken() ?: return null

        return try {
            val payload = token.split(".")[1]
            val decoded = String(android.util.Base64.decode(payload, android.util.Base64.URL_SAFE))
            val json = org.json.JSONObject(decoded)
            json.getInt("id")
        } catch (e: Exception) {
            null
        }
    }
}