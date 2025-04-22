package com.socialseller.bookpujari.viewModel

import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.socialseller.bookpujari.apiResponce.auth.CityResponce
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.apiResponce.auth.SignupResponce
import com.socialseller.bookpujari.apiResponce.auth.StateResponce
import com.socialseller.bookpujari.repository.AuthRepository
import com.socialseller.clothcrew.apiResponce.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _loginUser = MutableStateFlow<ApiResponse<LoginResponce>>(ApiResponse.Loading())
    val loginUser: StateFlow<ApiResponse<LoginResponce>> = _loginUser

    // Change here: SharedFlow with no replay, not StateFlow
    private val _signupEvent = MutableSharedFlow<ApiResponse<SignupResponce>>(replay = 0)
    val signupEvent = _signupEvent.asSharedFlow()

    private val _verifyEvent = MutableSharedFlow<ApiResponse<LoginResponce>>(replay = 0)
    val verifyEvent = _verifyEvent.asSharedFlow()

    private val _stateList = MutableSharedFlow<ApiResponse<StateResponce>>(replay = 0)
    val stateList = _stateList.asSharedFlow()

    private val _cityLiat = MutableSharedFlow<ApiResponse<CityResponce>>(replay = 0)
    val cityLiat = _cityLiat.asSharedFlow()

    val authStatus = liveData {
        val token = dataStoreManager.getString(Constants.KEY_TOKEN).firstOrNull()
        val profession = dataStoreManager.getString(Constants.KEY_USER_PROFESSION).firstOrNull()
        emit(Pair(token, profession))
    }

    init {
        allState()
    }

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
                saveUserData(response)
                _verifyEvent.emit(response) // Emit success/error only once
            } catch (e: Exception) {
                _verifyEvent.emit(ApiResponse.Error("Unexpected error: ${e.message}"))
            }
        }
    }

    fun allState() {
        viewModelScope.launch {
            _stateList.emit(ApiResponse.Loading()) // Emit loading state
            try {
                val response = authRepository.allState()
                _stateList.emit(response) // Emit success/error only once
            } catch (e: Exception) {
                _stateList.emit(ApiResponse.Error("Unexpected error: ${e.message}"))
            }
        }
    }

    fun allCity(state: String) {
        viewModelScope.launch {
            _cityLiat.emit(ApiResponse.Loading()) // Emit loading state
            try {
                val response = authRepository.allCity(state)
                _cityLiat.emit(response) // Emit success/error only once
            } catch (e: Exception) {
                _cityLiat.emit(ApiResponse.Error("Unexpected error: ${e.message}"))
            }
        }
    }

    private suspend fun saveUserData(response: ApiResponse<LoginResponce>) {
        if (response is ApiResponse.Success) {
            dataStoreManager.putString(Constants.KEY_TOKEN, response.data?.token ?: "")
            dataStoreManager.putString(Constants.KEY_USER_NAME, response.data?.data?.username ?: "")
            dataStoreManager.putString(Constants.KEY_USER_EMAIL, response.data?.data?.email ?: "")
            dataStoreManager.putString(Constants.KEY_USER_PHONE, response.data?.data?.phone ?: "")
            dataStoreManager.putString(Constants.KEY_USER_GENDER, response.data?.data?.gender ?: "")
            dataStoreManager.putString(Constants.KEY_USER_DOB, response.data?.data?.dob ?: "")
            dataStoreManager.putString(Constants.KEY_USER_CITY, response.data?.data?.city ?: "")
            dataStoreManager.putString(Constants.KEY_USER_STATE, response.data?.data?.state ?: "")
            dataStoreManager.putString(Constants.KEY_USER_MARITAL_STATUS, response.data?.data?.marital_status.toString() ?: "")
            dataStoreManager.putString(Constants.KEY_USER_PROFESSION, response.data?.data?.profession.toString() ?: "")
        }
    }

}
