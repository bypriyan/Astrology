package com.socialseller.bookpujari.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.auth.SignupResponce
import com.socialseller.bookpujari.repository.AuthRepository
import com.socialseller.clothcrew.apiResponce.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginUser = MutableStateFlow<ApiResponse<LoginResponce>>(ApiResponse.Loading())
    val loginUser: StateFlow<ApiResponse<LoginResponce>> = _loginUser

    // Change here: SharedFlow with no replay, not StateFlow
    private val _signupEvent = MutableSharedFlow<ApiResponse<SignupResponce>>(replay = 0)
    val signupEvent = _signupEvent.asSharedFlow()

    private val _verifyEvent = MutableSharedFlow<ApiResponse<LoginResponce>>(replay = 0)
    val verifyEvent = _verifyEvent.asSharedFlow()

    fun loginUser(userName: String, password: String) {
        viewModelScope.launch {
            _loginUser.value = ApiResponse.Loading()
            try {
                val response = if (Constants.isPhoneNumber(userName)) {
                    authRepository.loginUserWithPhoneNumber(userName, password)
                } else {
                    authRepository.loginUserWithEmail(userName, password)
                }
                _loginUser.value = response
            } catch (e: Exception) {
                _loginUser.value = ApiResponse.Error("Unexpected error: ${e.message}")
            }
        }
    }

    fun signup(email: String, phone: String) {
        viewModelScope.launch {
            _signupEvent.emit(ApiResponse.Loading()) // Emit loading state
            try {
                val response = authRepository.signup(email = email, phone = phone)
                _signupEvent.emit(response) // Emit success/error only once
            } catch (e: Exception) {
                _signupEvent.emit(ApiResponse.Error("Unexpected error: ${e.message}"))
            }
        }
    }

    fun verifyUser(username: String, email: String, password: String, phone: String, gender: String, dob: String, otp: String) {
        viewModelScope.launch {
            _verifyEvent.emit(ApiResponse.Loading()) // Emit loading state
            try {
                val response = authRepository.verifyUser(username = username, email = email, password = password, phone = phone, gender = gender, dob = dob, otp = otp)
                _verifyEvent.emit(response) // Emit success/error only once
            } catch (e: Exception) {
                _verifyEvent.emit(ApiResponse.Error("Unexpected error: ${e.message}"))
            }
        }
    }

}
