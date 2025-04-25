package com.socialseller.bookpujari.apiResponce.catrgory

data class CategoryResponce(
    val `data`: List<Data>,
    val message: String,
    val pagination: Pagination
)