package com.example.android.di

import com.example.android.data.remote.AuthInterceptor
import com.example.android.data.remote.AuthService
import com.example.android.data.remote.FollowService
import com.example.android.data.remote.UserService
import com.example.android.data.repository.AuthRepository
import com.example.android.data.repository.AuthRepositoryImpl
import com.example.android.data.repository.FollowRepository
import com.example.android.data.repository.FollowRepositoryImpl
import com.example.android.data.repository.UserRepository
import com.example.android.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides @Singleton
    fun provideAuthRepository(api: AuthService): AuthRepository =
        AuthRepositoryImpl(api)

    @Provides @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides @Singleton
    fun provideUserRepository(api: UserService): UserRepository =
        UserRepositoryImpl(api)

    @Provides @Singleton
    fun provideFollowService(retrofit: Retrofit): FollowService =
        retrofit.create(FollowService::class.java)

    @Provides @Singleton
    fun provideFollowRepository(api: FollowService): FollowRepository =
        FollowRepositoryImpl(api)

}