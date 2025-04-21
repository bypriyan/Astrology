package com.socialseller.bookpujari.repository

import com.socialseller.bookpujari.api.ApiAuth
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.auth.SignupResponce
import com.socialseller.clothcrew.apiResponce.ApiResponse
import com.socialseller.clothcrew.utility.ResponceHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val apiAuth: ApiAuth) {
    suspend fun loginUserWithPhoneNumber(phone: String, password: String): ApiResponse<LoginResponce> {
        return ResponceHelper.safeApiCall { apiAuth.loginUserWithPhoneNumber(phone, password) }
    }

    suspend fun loginUserWithEmail(email: String, password: String): ApiResponse<LoginResponce> {
        return ResponceHelper.safeApiCall { apiAuth.loginUserWithEmail(email, password) }
    }

    suspend fun signup(email: String, phone: String ): ApiResponse<SignupResponce> {
        return ResponceHelper.safeApiCall { apiAuth.signup( email = email, phone = phone) }
    }

    suspend fun verifyUser(username: String, email: String, password: String, phone: String, gender: String, dob: String, otp: String
    ): ApiResponse<LoginResponce> {
        return ResponceHelper.safeApiCall { apiAuth.verifyUser(
           username = username, email=email, password =password, phone = phone, gender = gender, dob = dob, otp = otp) }
    }

}