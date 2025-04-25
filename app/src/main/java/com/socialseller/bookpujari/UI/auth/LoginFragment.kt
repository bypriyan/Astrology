package com.socialseller.bookpujari.UI.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.material.textfield.TextInputEditText
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentLoginBinding
import com.socialseller.bookpujari.databinding.FragmentOTPBinding
import com.socialseller.bookpujari.viewModel.AuthViewModel
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue
import kotlin.toString

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeLoginResponse()
    }

    private fun observeLoginResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.loginUser.collectLatest { response ->
                    toggleLoading(false)
                    ResponceHelper.handleApiResponse(
                        response,
                        onSuccess = {
                            toggleLoading(false)
                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        },
                        onError = {
                            toggleLoading(false)
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                  },
                        logTag = "login"
                    )
                }
            }
        }
    }

    private fun initListeners() = with(binding) {
        requestOTPBtn.setOnClickListener { onLoginClicked() }
        registerLayout.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_UserNamesFragment)
        }
    }

    private fun onLoginClicked() {
        val phoneOrEmail = binding.phoneNumberET.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()

        if (validateInputs(phoneOrEmail, password)) {
            toggleLoading(true)
            authViewModel.loginUser(phoneOrEmail, password)
        }
    }

    private fun toggleLoading(isLoading: Boolean) = with(binding) {
        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        requestOTPBtn.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun validateInputs(phoneOrEmail: String, password: String): Boolean {
        var isValid = true

        // Validate email or phone
        if (phoneOrEmail.isEmpty()) {
            binding.phoneNumberET.error = "Please enter email or mobile number"
            isValid = false
        } else if (!Constants.isValidEmail(phoneOrEmail) && !Constants.isPhoneNumber(phoneOrEmail)) {
            binding.phoneNumberET.error = "Invalid email or mobile number"
            isValid = false
        } else {
            binding.phoneNumberET.error = null
        }

        // Validate password
        if (password.isEmpty()) {
            binding.passwordEt.error = "Please enter password"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordEt.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.passwordEt.error = null
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
