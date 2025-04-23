package com.socialseller.bookpujari.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.auth.SignupResponce
import com.socialseller.bookpujari.apiResponce.profile.ProfileResponce
import com.socialseller.bookpujari.repository.UserRepository
import com.socialseller.clothcrew.apiResponce.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _profileUpdate = MutableSharedFlow<ApiResponse<ProfileResponce>>(replay = 0)
    val profileUpdate = _profileUpdate.asSharedFlow()

    fun fetchTokenAndUpdateProfile(imageFile: File?, city: String, state: String, profession: String, maritalStatus: String) {
        viewModelScope.launch {
            dataStoreManager.getString(Constants.KEY_TOKEN).collect { token ->
                token?.let {
                    updateProfile(it, imageFile, city, state, profession, maritalStatus)
                }
            }
        }
    }

    fun updateProfile(
        token: String,
        imageFile: File?,
        city: String,
        state: String,
        profession: String,
        maritalStatus: String) {
        viewModelScope.launch {
            _profileUpdate.emit(ApiResponse.Loading())
            try {
                val response = userRepository.updateProfile(token = token, imageFile, city, state, profession, maritalStatus)

                saveUserData(response)
                _profileUpdate.emit(response)
            } catch (e: Exception) {
                _profileUpdate.emit(ApiResponse.Error("Unexpected error: ${e.message}"))
            }
        }
    }

    private suspend fun saveUserData(response: ApiResponse<ProfileResponce>) {
        if (response is ApiResponse.Success) {
            dataStoreManager.putString(Constants.KEY_USER_CITY, response.data?.data?.city ?: "")
            dataStoreManager.putString(Constants.KEY_USER_STATE, response.data?.data?.state ?: "")
            dataStoreManager.putString(Constants.KEY_USER_MARITAL_STATUS, response.data?.data?.marital_status.toString() ?: "")
            dataStoreManager.putString(Constants.KEY_USER_PROFESSION, response.data?.data?.profession.toString() ?: "")
        }
    }

    fun cleadStoredData(){
        viewModelScope.launch {
            dataStoreManager.clear()
        }
    }



}