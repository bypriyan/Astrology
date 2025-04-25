package com.socialseller.bookpujari.api

import com.socialseller.bookpujari.apiResponce.catrgory.CategoryResponce
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCategory {
    @GET("category")
    suspend fun category(
        @Query("page") page: Int=1,
        @Query("limit") limit: Int=20
    ): Response<CategoryResponce>
}