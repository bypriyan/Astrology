package com.socialseller.bookpujari.repository

import android.util.Log
import com.socialseller.bookpujari.api.ApiAuth
import com.socialseller.bookpujari.api.ApiUser
import com.socialseller.bookpujari.apiResponce.auth.CityResponce
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.auth.SignupResponce
import com.socialseller.bookpujari.apiResponce.auth.StateResponce
import com.socialseller.bookpujari.apiResponce.profile.ProfileResponce
import com.socialseller.bookpujari.apiResponce.user.SingleUserResponce
import com.socialseller.clothcrew.apiResponce.ApiResponse
import com.socialseller.clothcrew.utility.ResponceHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val apiUser: ApiUser) {

    suspend fun updateProfile(
        token: String,
        profileImage: File?,
        city: String,
        state: String,
        profession: String,
        maritalStatus: String):  ApiResponse<ProfileResponce>{

        val imagePart = profileImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profile_image", it.name, requestFile)
        }
        val respo = apiUser.updateUserProfile(
            token = "Bearer $token",
            profile_image = imagePart,
            city = city.toRequestBody("text/plain".toMediaTypeOrNull()),
            state = state.toRequestBody("text/plain".toMediaTypeOrNull()),
            profession = profession.toRequestBody("text/plain".toMediaTypeOrNull()),
            maritalStatus = maritalStatus.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        return ResponceHelper.safeApiCall {
            respo
        }
    }

    suspend fun getUserDetails(token: String):  ApiResponse<SingleUserResponce>{
        return ResponceHelper.safeApiCall {
            apiUser.getSingleUserData(token = "Bearer $token")
        }
    }

}