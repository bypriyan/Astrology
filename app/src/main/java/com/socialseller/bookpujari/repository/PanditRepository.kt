package com.socialseller.bookpujari.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.socialseller.bookpujari.PagingSource.CategoryPagingSource
import com.socialseller.bookpujari.PagingSource.PanditPagingSource
import com.socialseller.bookpujari.api.ApiCategory
import com.socialseller.bookpujari.api.ApiPandit
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.catrgory.Data
import com.socialseller.bookpujari.apiResponce.pandit.Pandit
import com.socialseller.bookpujari.apiResponce.pandit.singlePandit.SinglePanditResponce
import com.socialseller.clothcrew.apiResponce.ApiResponse
import com.socialseller.clothcrew.utility.ResponceHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PanditRepository @Inject constructor(
    private val apiPandit: ApiPandit
) {

    fun getPandits(city: String, state: String): Flow<PagingData<Pandit>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PanditPagingSource(apiPandit, city, state) }
        ).flow
    }

    suspend fun getSinglePanditDetails(id: Int): ApiResponse<SinglePanditResponce> {
        return ResponceHelper.safeApiCall { apiPandit.getPanditDetails(id)}
    }


}
