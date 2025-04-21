package com.socialseller.bookpujari.UI.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentOTPBinding
import com.socialseller.bookpujari.databinding.FragmentRegistrationBinding
import com.socialseller.bookpujari.sharedViewModel.RegistrationViewModel
import com.socialseller.bookpujari.viewModel.AuthViewModel
import com.socialseller.clothcrew.utility.KeyboardUtils
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.toString

@AndroidEntryPoint
class OTPFragment : Fragment(R.layout.fragment_o_t_p) {

    private var _binding: FragmentOTPBinding? = null
    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)
    private val authViewModel: AuthViewModel by viewModels()
    private var username = ""
    private var phoneNumber = ""
    private var email = ""
    private var password = ""
    private var gender = ""
    private var dob = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeOTPResponce()
        binding.apply {
        }
    }

    private fun setupUI() {
        registrationViewModel.registrationData.observe(viewLifecycleOwner) { registrationData ->
            registrationData?.let { data ->
                Log.d("otp", "setupUI: $data")
               username = data.fullName
                phoneNumber = data.mobileNumber
                email = data.email
                password = data.password
                gender = data.gender
                dob = data.dob
            }
        }

        setupPhoneNumberListener()
        binding.apply {
            requestOTPBtn.setOnClickListener { handleOtpRequest() }
        }
    }

    private fun setupPhoneNumberListener() {
        binding.otpET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isValid = s?.length == 4
                binding.requestOTPBtn.apply {
                    isEnabled = isValid
                    alpha = if (isValid) 1.0f else 0.5f
                }

                if (isValid) {
                    KeyboardUtils.hideKeyboard(requireContext(), binding.otpET)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun handleOtpRequest() {
        val otp = binding.otpET.text?.toString()?.trim()
        if (otp.isNullOrEmpty() || otp.length != 4) {
            Toast.makeText(
                requireContext(),
                "Please enter a valid OTP",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        toggleLoading(true)
        // Navigate to OtpFragment
        authViewModel.verifyUser(username = username, email = email, password = password, phone = phoneNumber, gender = gender, dob = dob, otp = otp)
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding.apply {
            if(isLoading){
                progressbar.visibility = View.VISIBLE
                requestOTPBtn.visibility = View.GONE
            }else{
                progressbar.visibility = View.GONE
                requestOTPBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun observeOTPResponce() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.verifyEvent.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        toggleLoading(false)
                        findNavController().navigate(R.id.action_otpFragment_to_profileDetailsFragment)
                    },
                    onError = { error ->
                        toggleLoading(false)
                    },
                    logTag = "Login"
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}