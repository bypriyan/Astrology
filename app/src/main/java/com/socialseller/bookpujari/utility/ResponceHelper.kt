package com.socialseller.clothcrew.utility

import android.util.Log
import com.socialseller.clothcrew.apiResponce.ApiResponse
import retrofit2.Response

object ResponceHelper {
     fun <T> handleApiResponse(
        response: ApiResponse<T>,
        onSuccess: (T) -> Unit,
        onError: ((String) -> Unit),
        logTag: String
    ) {
        when (response) {
            is ApiResponse.Loading -> {
                // Show a loading state if needed
            }
            is ApiResponse.Success -> {
                response.data?.let(onSuccess)
            }
            is ApiResponse.Error -> {
                onError(response.message ?: "Unknown error")
                Log.d(logTag, "Error fetching $logTag: ${response.message}")
            }
        }
    }

     suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                ApiResponse.Success(response.body()!!)
            } else {
                ApiResponse.Error(HttpStatusHelper.getMessage(response.code()), null)
            }
        } catch (e: Exception) {
            ApiResponse.Error("Network Error: ${e.message}")
        }
    }

}
