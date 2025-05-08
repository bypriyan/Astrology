package com.socialseller.bookpujari.apiResponce.pandit.singlePandit

data class Data(
    val about: String,
    val averageRating: String,
    val chatRatePerMinute: String,
    val city: String,
    val email: String,
    val experienceYears: String,
    val id: String,
    val intro_video_url: String,
    val languagesSpoken: List<String>,
    val phone: String,
    val profile_image: String,
    val ratingCount: String,
    val rating_summary: RatingSummary,
    val service_type: List<ServiceType>,
    val state: String,
    val username: String
)