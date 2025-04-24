package com.socialseller.bookpujari.apiResponce.auth

data class StateResponce(
    val `data`: List<DataX>,
    val message: String,
    val success: Boolean
)