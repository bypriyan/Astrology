package com.socialseller.bookpujari.api

import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.profile.ProfileResponce
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface ApiUser {
    @Multipart
    @PATCH("users/profile-update")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Part profile_image: MultipartBody.Part?,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("profession") profession: RequestBody,
        @Part("marital_status") maritalStatus: RequestBody,
    ):Response<ProfileResponce>
}