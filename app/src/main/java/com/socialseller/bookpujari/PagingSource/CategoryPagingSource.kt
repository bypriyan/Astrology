package com.socialseller.bookpujari.PagingSource

import android.util.Log
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
        val page = params.key ?: 1
        return try {
            val response = ResponceHelper.safeApiCall {
                apiCategory.category(page = page, limit = 20)
            }

            when (response) {
                is ApiResponse.Success -> {
                    val data = response.data?.data ?: emptyList()
                    val pagination = response.data?.pagination
                    val totalItems = pagination?.totalItems ?: 0
                    val itemsLoaded = (page - 1) * params.loadSize + data.size
                    Log.d("CategoryPagingSource", "Loaded ${data.size} items on page $page (Total so far: $itemsLoaded)")
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty() || itemsLoaded >= totalItems) null else page + 1
                    )
                }

                is ApiResponse.Error -> LoadResult.Error(Exception(response.message))
                is ApiResponse.Loading -> LoadResult.Page(emptyList(), null, null)
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
