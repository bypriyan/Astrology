package com.bypriyan.bustrackingsystem.utility

import android.content.Context
import android.util.Patterns
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.socialseller.bookpujari.apiResponce.pandit.ServiceType
import com.socialseller.bookpujari.apiResponce.user.UserProfile


object Constants{
    const val KEY_RESPONCE_SUCCESS= "success"
    const val KEY_RESPONCE_FAILED= "failed"
    //user
    const val KEY_TOKEN = "token"
    const val KEY_USER_NAME= "userName"
    const val KEY_USER_EMAIL= "userEmail"
    const val KEY_USER_PHONE = "userPhone"
    const val KEY_USER_GENDER = "userGender"
    const val KEY_USER_DOB = "userDOB"
    const val KEY_USER_CITY = "userCity"
    const val KEY_USER_STATE = "userState"
    const val KEY_USER_MARITAL_STATUS = "userMaritalStatus"
    const val KEY_USER_PROFESSION = "userProfession"

    fun isPhoneNumber(input: String): Boolean {
        return input.matches(Regex("^\\d{10}$")) // Checks for exactly 10 digits
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun <T> setupDropdown(
        view: MaterialAutoCompleteTextView,
        items: List<T>,
        context: Context,
        displayProperty: (T) -> String = { it.toString() }, // For String lists, toString() works naturally
        onSelect: (selectedItem: T) -> Unit
    ) {
        val displayItems = items.map { displayProperty(it) }

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, displayItems)
        view.setAdapter(adapter)
        view.threshold = 1

        view.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            view.clearFocus()
            onSelect(selectedItem)
        }
    }

    // utils/UserDataStoreExtensions.kt
    suspend fun DataStoreManager.saveUserProfile(profile: UserProfile) {
        putString(Constants.KEY_USER_NAME, profile.username)
        putString(Constants.KEY_USER_EMAIL, profile.email)
        putString(Constants.KEY_USER_PHONE, profile.phone)
        putString(Constants.KEY_USER_GENDER, profile.gender)
        putString(Constants.KEY_USER_DOB, profile.dob)
        putString(Constants.KEY_USER_CITY, profile.city)
        putString(Constants.KEY_USER_STATE, profile.state)
        putString(Constants.KEY_USER_MARITAL_STATUS, profile.maritalStatus)
        putString(Constants.KEY_USER_PROFESSION, profile.profession)
        profile.token?.let {
            putString(Constants.KEY_TOKEN, it)
        }
    }

    fun concatenateList(list: List<String>, separator: String ): String {
        return list.joinToString(separator = separator)
    }

    fun concatenateServiceNamesWithPipe(list: List<com.socialseller.bookpujari.apiResponce.pandit.singlePandit.ServiceType>): String {
        return list.joinToString(separator = " | ") { it.name }
    }



}