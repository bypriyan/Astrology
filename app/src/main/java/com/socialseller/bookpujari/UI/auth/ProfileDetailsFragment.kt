package com.socialseller.bookpujari.UI.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentOTPBinding
import com.socialseller.bookpujari.databinding.FragmentProfileDetailsBinding
import com.socialseller.bookpujari.viewModel.AuthViewModel
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    companion object {
        private const val IMAGE_MIME_TYPE = "image/*"
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { displaySelectedImage(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeStateList()
        observeCityList()
    }

    private fun initUI() = with(binding) {
        profileFrame.setOnClickListener { openGallery() }

        Constants.setupDropdown(
            meritalStatusEditText,
            listOf("Single", "Married", "Divorced", "Widowed", "Separated", "Other"),
            requireContext()
        ) { selected ->
            // Handle merital status selection if needed
        }
        requestOtpButton.setOnClickListener {
            if (isValid()) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()}
        }
    }

    private fun observeStateList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateList.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        Constants.setupDropdown(
                            binding.stateEditText,
                            it.states,
                            requireContext()
                        ) { selectedState ->
                            binding.cityEditText.text= null
                            viewModel.allCity(selectedState)
                        }
                    },
                    onError = {
                        Log.e("ProfileDetailsFragment", "State error: $it")
                    },
                    logTag = "stateList"
                )
            }
        }
    }

    private fun observeCityList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cityLiat.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        Constants.setupDropdown(
                            binding.cityEditText,
                            it.cities,
                            requireContext()
                        ) { selectedCity ->
                            // Handle selected city if needed
                        }
                    },
                    onError = {
                        Log.e("ProfileDetailsFragment", "City error: $it")
                    },
                    logTag = "cityList"
                )
            }
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch(IMAGE_MIME_TYPE)
    }

    private fun isValid(): Boolean {
        var isValid = true

        with(binding) {
            // Validate State
            val state = stateEditText.text.toString().trim()
            if (state.isEmpty()) {
                stateInputLayout4.error = "Please select your state"
                isValid = false
            } else {
                stateInputLayout4.error = null
            }

            // Validate City
            val city = cityEditText.text.toString().trim()
            if (city.isEmpty()) {
                cityInputLayout4.error = "Please select your city"
                isValid = false
            } else {
                cityInputLayout4.error = null
            }

            // Validate Marital Status
            val maritalStatus = meritalStatusEditText.text.toString().trim()
            if (maritalStatus.isEmpty()) {
                MaritalStatusInputLayout4.error = "Please select your marital status"
                isValid = false
            } else {
                MaritalStatusInputLayout4.error = null
            }

            // Validate Profession
            val profession = ProfessionEditText.text.toString().trim()
            when {
                profession.isEmpty() -> {
                    ProfessionInputLayout.error = "Please enter your profession"
                    isValid = false
                }
                profession.length < 3 -> {
                    ProfessionInputLayout.error = "Profession should be at least 3 characters"
                    isValid = false
                }
                else -> {
                    ProfessionInputLayout.error = null
                }
            }

            // Validate Profile Image
            if (galleryImage.visibility == View.VISIBLE) {
                Toast.makeText(requireContext(), "Please select a profile image", Toast.LENGTH_LONG).show()
                isValid = false
            }
        }

        return isValid
    }

    private fun displaySelectedImage(imageUri: Uri) = with(binding) {
        profileImage.setImageURI(imageUri)
        profileImage.visibility = View.VISIBLE
        galleryImage.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
