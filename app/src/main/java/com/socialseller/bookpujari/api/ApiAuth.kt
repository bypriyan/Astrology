package com.socialseller.bookpujari.api

import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.auth.SignupResponce
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiAuth {
    @FormUrlEncoded
    @POST("users/login")
    suspend fun loginUserWithPhoneNumber(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<LoginResponce>

    @FormUrlEncoded
    @POST("users/login")
    suspend fun loginUserWithEmail(
        @Field("email") phone: String,
        @Field("password") password: String
    ): Response<LoginResponce>

    @FormUrlEncoded
    @POST("users")
    suspend fun signup(
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Response<SignupResponce>

    @FormUrlEncoded
    @POST("users/verify")
    suspend fun verifyUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("country_code") country_code: String = "+91",
        @Field("gender") gender: String,
        @Field("dob") dob: String,
        @Field("city") city: String="null",
        @Field("role") role: String = "user",
        @Field("otp") otp: String
    ): Response<LoginResponce>



}