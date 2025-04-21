package com.socialseller.bookpujari.sharedViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.socialseller.bookpujari.sharedModel.RegistrationData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor() : ViewModel() {

    // Registration data
    private val _registrationData = MutableLiveData<RegistrationData>()
    val registrationData: LiveData<RegistrationData> = _registrationData

    fun setRegistrationData(
        name: String, dobValue: String, genderValue: String,
        mobile: String, emailId: String, pwd: String
    ) {
        _registrationData.value = RegistrationData(name, dobValue, genderValue, mobile, emailId, pwd)
    }
}
