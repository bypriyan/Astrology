package com.socialseller.bookpujari.UI.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.apiResponce.auth.LoginResponce
import com.socialseller.bookpujari.databinding.FragmentOTPBinding
import com.socialseller.bookpujari.sharedViewModel.RegistrationViewModel
import com.socialseller.bookpujari.viewModel.AuthViewModel
import com.socialseller.clothcrew.apiResponce.ApiResponse
import com.socialseller.clothcrew.utility.KeyboardUtils
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class OTPFragment : Fragment(R.layout.fragment_o_t_p) {

    private var _binding: FragmentOTPBinding? = null
    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by navGraphViewModels(R.id.nav_graph)
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var userInfo: UserInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeOTPResponse()
    }

    private fun setupUI() {
        registrationViewModel.registrationData.observe(viewLifecycleOwner) { data ->
            data?.let {
                Log.d("otp", "setupUI: $it")
                userInfo = UserInfo(
                    fullName = it.fullName,
                    mobileNumber = it.mobileNumber,
                    email = it.email,
                    password = it.password,
                    gender = it.gender,
                    dob = it.dob
                )
            }
        }

        setupOTPFieldListener()

        binding.requestOTPBtn.setOnClickListener {
            handleOtpRequest()
        }
    }

    private fun setupOTPFieldListener() {
        binding.otpET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isValid = s?.length == 4
                with(binding.requestOTPBtn) {
                    isEnabled = isValid
                    alpha = if (isValid) 1f else 0.5f
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
            Toast.makeText(requireContext(), "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
            return
        }

        toggleLoading(true)

        authViewModel.verifyUser(
            username = userInfo.fullName,
            email = userInfo.email,
            password = userInfo.password,
            phone = userInfo.mobileNumber,
            gender = userInfo.gender,
            dob = userInfo.dob,
            otp = otp
        )
    }

    private fun toggleLoading(isLoading: Boolean) {
        with(binding) {
            progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
            requestOTPBtn.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun observeOTPResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.verifyEvent.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        toggleLoading(false)
                        findNavController().navigate(R.id.action_otpFragment_to_profileDetailsFragment)
                    },
                    onError = {
                        toggleLoading(false)
                        // Optionally: show a Toast or Snackbar with error
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

    data class UserInfo(
        val fullName: String,
        val mobileNumber: String,
        val email: String,
        val password: String,
        val gender: String,
        val dob: String
    )
}
