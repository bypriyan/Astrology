package com.socialseller.bookpujari.apiResponce.auth

data class Data(
    val city: String,
    val country_code: String,
    val dob: String,
    val email: String,
    val gender: String,
    val id: Int,
    val marital_status: Any,
    val phone: String,
    val profession: Any,
    val role: String,
    val state: String?,
    val username: String
)