package com.bypriyan.bustrackingsystem.utility

import android.util.Patterns


object Constants{
    const val KEY_RESPONCE_SUCCESS= "success"
    const val KEY_RESPONCE_FAILED= "failed"

    fun isPhoneNumber(input: String): Boolean {
        return input.matches(Regex("^\\d{10}$")) // Checks for exactly 10 digits
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}