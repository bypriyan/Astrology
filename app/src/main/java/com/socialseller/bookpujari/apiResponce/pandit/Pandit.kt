package com.socialseller.bookpujari.apiResponce.pandit

data class Pandit(
    val averageRating: Double,
    val chatRatePerMinute: String,
    val city: String,
    val experienceYears: Int,
    val id: Int,
    val languagesSpoken: List<String>,
    val profile_image: String,
    val service_type: List<ServiceType>,
    val state: String,
    val username: String
)