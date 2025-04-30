package com.socialseller.bookpujari.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bypriyan.bustrackingsystem.utility.Constants
import com.socialseller.bookpujari.apiResponce.auth.StateResponce
import com.socialseller.bookpujari.apiResponce.pandit.Pandit
import com.socialseller.bookpujari.apiResponce.pandit.singlePandit.SinglePanditResponce
import com.socialseller.bookpujari.repository.PanditRepository
import com.socialseller.clothcrew.apiResponce.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanditViewModel @Inject constructor(
    private val repository: PanditRepository
) : ViewModel() {

    private val _singlePandit = MutableSharedFlow<ApiResponse<SinglePanditResponce>>(replay = 0)
    val singlePandit = _singlePandit.asSharedFlow()

    fun getPandits(city: String, state: String): Flow<PagingData<Pandit>> {
        return repository.getPandits(city, state).cachedIn(viewModelScope)
    }

    fun getSinglePandit(id: Int) {
        viewModelScope.launch {
            _singlePandit.emit(ApiResponse.Loading())
            try {
                val response = repository.getSinglePanditDetails(id)
                _singlePandit.emit(response)
            } catch (e: Exception) {
                _singlePandit.emit(ApiResponse.Error("Unexpected error: ${e.message}"))
            }
        }
    }

}