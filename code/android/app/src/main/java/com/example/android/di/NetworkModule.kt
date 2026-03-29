package com.example.android.di

import com.example.android.data.remote.AuthService
import com.example.android.data.repository.UserRepository
import com.example.android.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    val api_url = System.getenv("API_URL") ?: ""
    @Provides
    @Singleton
    fun init() : Retrofit {


        return Retrofit.Builder()
            .baseUrl(api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides @Singleton
    fun provideUserApi(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides @Singleton
    fun provideUserRepository(api: AuthService): UserRepository =
        UserRepositoryImpl(api)
}