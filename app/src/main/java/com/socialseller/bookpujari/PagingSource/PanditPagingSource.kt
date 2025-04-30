package com.socialseller.bookpujari.PagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.socialseller.bookpujari.api.ApiPandit
import com.socialseller.bookpujari.apiResponce.pandit.Pandit

class PanditPagingSource(
    private val apiPandit: ApiPandit,
    private val city: String,
    private val state: String
) : PagingSource<Int, Pandit>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pandit> {
        return try {
            val page = params.key ?: 1
            Log.d("PanditPaging", "Loading page: $page") // 👈 Log page number
            val response = apiPandit.getPandits(page = page, city = city, state = state)
            val responseData = response.body()

            if (response.isSuccessful && responseData != null) {
                val loadedSize = responseData.pandits.size
                Log.d("PanditPaging", "Page $page loaded with $loadedSize pandits")
                LoadResult.Page(
                    data = responseData.pandits,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (responseData.pandits.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pandit>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}