package com.socialseller.bookpujari.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.socialseller.bookpujari.PagingSource.CategoryPagingSource
import com.socialseller.bookpujari.api.ApiCategory
import com.socialseller.bookpujari.apiResponce.catrgory.CategoryResponce
import com.socialseller.bookpujari.apiResponce.catrgory.Data
import com.socialseller.clothcrew.apiResponce.ApiResponse
import com.socialseller.clothcrew.utility.ResponceHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val apiCategory: ApiCategory
) {
    fun getPagedCategories(): Pager<Int, Data> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CategoryPagingSource(apiCategory) }
        )
    }
}
