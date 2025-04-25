package com.socialseller.bookpujari.apiResponce.user

data class UserProfile(
    val username: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: String = "",
    val dob: String = "",
    val city: String = "",
    val state: String = "",
    val maritalStatus: String = "",
    val profession: String = "",
    val token: String? = null // optional
)
