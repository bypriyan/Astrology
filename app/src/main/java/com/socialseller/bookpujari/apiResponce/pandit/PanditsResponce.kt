package com.socialseller.bookpujari.apiResponce.pandit

data class PanditsResponce(
    val message: String,
    val pagination: Pagination,
    val pandits: List<Pandit>
)