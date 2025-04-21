package com.socialseller.bookpujari.apiResponce.auth

data class LoginResponce(
    val `data`: Data,
    val message: String,
    val token: String
)