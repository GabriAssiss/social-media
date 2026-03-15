package com.example.android.di

import com.example.android.data.remote.UserService
import com.example.android.data.repository.UserRepository
import com.example.android.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun init() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.18.7:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides @Singleton
    fun provideUserApi(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides @Singleton
    fun provideUserRepository(api: UserService): UserRepository =
        UserRepositoryImpl(api)
}