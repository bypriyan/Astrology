package com.socialseller.bookpujari.PagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.socialseller.bookpujari.api.ApiCategory
import com.socialseller.bookpujari.apiResponce.catrgory.Data
import com.socialseller.clothcrew.apiResponce.ApiResponse
import com.socialseller.clothcrew.utility.ResponceHelper

// PagingSource using ResponceHelper
class CategoryPagingSource(
    private val apiCategory: ApiCategory
) : PagingSource<Int, Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            val page = params.key ?: 1
            val response = ResponceHelper.safeApiCall {
                apiCategory.category(page, params.loadSize)
            }

            when (response) {
                is ApiResponse.Success -> {
                    val data = response.data?.data ?: emptyList()
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page + 1
                    )
                }

                is ApiResponse.Error -> {
                    LoadResult.Error(Exception(response.message))
                }

                is ApiResponse.Loading -> {
                    LoadResult.Page(emptyList(), null, null) // shouldn't hit this
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
