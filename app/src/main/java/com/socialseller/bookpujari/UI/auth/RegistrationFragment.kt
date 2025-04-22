package com.socialseller.bookpujari.UI.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentRegistrationBinding
import com.socialseller.bookpujari.databinding.FragmentWalkthrowBinding
import com.socialseller.bookpujari.sharedViewModel.RegistrationViewModel
import com.socialseller.bookpujari.viewModel.AuthViewModel
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.getValue

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)
    private val authViewModel: AuthViewModel by viewModels()

    var isNavigated = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Constants.setupDropdown(binding.genderEditText,
            listOf("Male", "Female", "Others"),
            requireContext()) {

        }// Move this here

        observeSignupResponce()

        binding.apply {
            loginPromptLayout.setOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            }
            dobEditText.setOnClickListener {
                showDatePickerDialog()
            }
            requestOTPBtn.setOnClickListener {
                if(validateInputs()){
                    toggleLoading(true)
                    val mobile = binding.mobileNumberEditText.text.toString()
                    val email = binding.emailEditText.text.toString()
                    authViewModel.signup(email = email, phone = mobile)
                }
            }
        }
    }


    private fun observeSignupResponce() {
        lifecycleScope.launch {
            authViewModel.signupEvent.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        Log.d("cld", "observeSignupResponce: called")
                        toggleLoading(false)
                            isNavigated = true // Prevent repeated nav
                            val name = binding.fullNameEditText.text.toString()
                            val dob = binding.dobEditText.text.toString()
                            val gender = binding.genderEditText.text.toString()
                            val mobile = binding.mobileNumberEditText.text.toString()
                            val email = binding.emailEditText.text.toString()
                            val password = binding.passwordEditText.text.toString()
                            registrationViewModel.setRegistrationData(name, dob, gender, mobile, email, password)
                            findNavController().navigate(R.id.action_registrationFragment_to_otpFragment)

                    },
                    onError = { errored ->
                        toggleLoading(false)
                        if (errored == "Bad Request"){
                            Toast.makeText(requireContext(), "User already registered. Please login.", Toast.LENGTH_LONG).show()
                            return@handleApiResponse
                        }else{
                            Toast.makeText(requireContext(), errored, Toast.LENGTH_LONG).show()
                            return@handleApiResponse
                        }

                    },
                    logTag = "login"
                )
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Set maximum date to 13 years ago from today
        calendar.add(Calendar.YEAR, -13)
        val maxDate = calendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.dobEditText.setText(selectedDate)
            },
            year,
            month,
            day
        ).apply {
            // Set maximum date to ensure user is at least 13 years old
            datePicker.maxDate = maxDate
        }
        datePickerDialog.show()
    }

    private fun toggleLoading(isLoading: Boolean) = with(binding) {
        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        requestOTPBtn.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Full Name validation
        binding.fullNameEditText.text?.let {
            if (it.isEmpty()) {
                binding.fullNameEditText.error = "Full name is required"
                isValid = false
            } else if (it.length < 3) {
                binding.fullNameEditText.error = "Name too short"
                isValid = false
            } else {
                binding.fullNameEditText.error = null
            }
        } ?: run {
            binding.fullNameEditText.error = "Full name is required"
            isValid = false
        }

        // Date of Birth validation with age check (minimum 13 years)
        binding.dobEditText.text?.let {
            if (it.isEmpty()) {
                binding.dobEditText.error = "Date of birth is required"
                isValid = false
            } else {
                try {
                    // Parse the date (assuming format is dd/MM/yyyy)
                    val dateParts = it.split("-")
                    if (dateParts.size != 3) {
                        binding.dobEditText.error = "Invalid date format (use DD/MM/YYYY)"
                        isValid = false
                    } else {
                        val day = dateParts[2].toInt()
                        val month = dateParts[1].toInt() - 1 // Calendar months are 0-based
                        val year = dateParts[0].toInt()

                        val dobCalendar = Calendar.getInstance().apply {
                            set(year, month, day)
                        }

                        val today = Calendar.getInstance()
                        var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

                        // Adjust age if birthday hasn't occurred yet this year
                        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                            age--
                        }

                        if (age < 13) {
                            binding.dobEditText.error = "You must be at least 13 years old"
                            isValid = false
                        } else {
                            binding.dobEditText.error = null
                        }
                    }
                } catch (e: Exception) {
                    binding.dobEditText.error = "Invalid date format"
                    isValid = false
                }
            }
        } ?: run {
            binding.dobEditText.error = "Date of birth is required"
            isValid = false
        }

        // Rest of the validations (gender, mobile, email, password) remain the same as before
        // Gender validation
        binding.genderEditText.text?.let {
            if (it.isEmpty()) {
                binding.genderEditText.error = "Gender is required"
                isValid = false
            } else {
                binding.genderEditText.error = null
            }
        } ?: run {
            binding.genderEditText.error = "Gender is required"
            isValid = false
        }

        // Mobile Number validation
        binding.mobileNumberEditText.text?.let {
            val mobileRegex = Regex("^[0-9]{10}$")
            if (it.isEmpty()) {
                binding.mobileNumberEditText.error = "Mobile number is required"
                isValid = false
            } else if (!it.matches(mobileRegex)) {
                binding.mobileNumberEditText.error = "Invalid mobile number"
                isValid = false
            } else {
                binding.mobileNumberEditText.error = null
            }
        } ?: run {
            binding.mobileNumberEditText.error = "Mobile number is required"
            isValid = false
        }

        // Email validation
        binding.emailEditText.text?.let {
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
            if (it.isEmpty()) {
                binding.emailEditText.error = "Email is required"
                isValid = false
            } else if (!it.matches(emailRegex)) {
                binding.emailEditText.error = "Invalid email format"
                isValid = false
            } else {
                binding.emailEditText.error = null
            }
        } ?: run {
            binding.emailEditText.error = "Email is required"
            isValid = false
        }

        // Password validation
        binding.passwordEditText.text?.let { password ->
            if (password.isEmpty()) {
                binding.passwordLayout.error = "Password is required"
                isValid = false
            } else if (password.length < 8) {
                binding.passwordLayout.error = "Password must be at least 8 characters"
                isValid = false
            } else {
                binding.passwordLayout.error = null

                // Confirm Password validation
                binding.confirmPasswordEditText.text?.let { confirmPassword ->
                    if (confirmPassword.isEmpty()) {
                        binding.confirmPasswordLayout.error = "Please confirm your password"
                        isValid = false
                    } else if (password.toString() != confirmPassword.toString()) {
                        binding.confirmPasswordLayout.error = "Passwords don't match"
                        isValid = false
                    } else {
                        binding.confirmPasswordLayout.error = null
                    }
                } ?: run {
                    binding.confirmPasswordLayout.error = "Please confirm your password"
                    isValid = false
                }
            }
        } ?: run {
            binding.passwordLayout.error = "Password is required"
            isValid = false
        }

        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}