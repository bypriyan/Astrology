package com.socialseller.bookpujari.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.Constants.saveUserProfile
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.auth.SignupResponce
import com.socialseller.bookpujari.apiResponce.profile.ProfileResponce
import com.socialseller.bookpujari.apiResponce.user.SingleUserResponce
import com.socialseller.bookpujari.apiResponce.user.UserProfile
import com.socialseller.bookpujari.repository.CategoryRepository
import com.socialseller.bookpujari.repository.UserRepository
import com.socialseller.clothcrew.apiResponce.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val categories = categoryRepository.getPagedCategories()
        .flow
        .cachedIn(viewModelScope)
}
