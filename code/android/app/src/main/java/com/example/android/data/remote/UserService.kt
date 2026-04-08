package com.example.android.data.remote

import ProfileResponse
import retrofit2.http.GET

interface UserService {

    @GET("/api/v1/users/me/profile")
    suspend fun getMyProfile() : ProfileResponse

}