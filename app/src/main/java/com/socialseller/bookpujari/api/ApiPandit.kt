package com.socialseller.bookpujari.api

import com.socialseller.bookpujari.apiResponce.catrgory.CategoryResponce
import com.socialseller.bookpujari.apiResponce.pandit.PanditsResponce
import com.socialseller.bookpujari.apiResponce.pandit.singlePandit.SinglePanditResponce
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiPandit {

    @GET("pandit/search")
    suspend fun getPandits(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("city") city: String,
        @Query("state") state: String,
    ): Response<PanditsResponce>

    @GET("pandit/{id}")
    suspend fun getPanditDetails(@Path("id") panditId: Int): Response<SinglePanditResponce>

}