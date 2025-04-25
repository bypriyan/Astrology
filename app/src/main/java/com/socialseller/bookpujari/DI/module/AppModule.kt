package com.bypriyan.togocartstore.DI.module

import com.google.gson.Gson
import com.socialseller.bookpujari.api.ApiAuth
import com.socialseller.bookpujari.api.ApiCategory
import com.socialseller.bookpujari.api.ApiUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://panditjiapi.mtlapi.socialseller.in/api/v1/") // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiAuth(retrofit: Retrofit): ApiAuth {
        return retrofit.create(ApiAuth::class.java)
    }

    @Provides
    @Singleton
    fun provideApiUser(retrofit: Retrofit): ApiUser {
        return retrofit.create(ApiUser::class.java)
    }

    @Provides
    @Singleton
    fun provideApiCategory(retrofit: Retrofit): ApiCategory {
        return retrofit.create(ApiCategory::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()


}