package com.socialseller.bookpujari.UI.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bypriyan.bustrackingsystem.utility.Constants
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentOTPBinding
import com.socialseller.bookpujari.databinding.FragmentProfileDetailsBinding
import com.socialseller.bookpujari.viewModel.AuthViewModel
import com.socialseller.bookpujari.viewModel.UserViewModel
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.getValue

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var selectedImageUri: Uri? = null

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

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeStateList()
        observeCityList()
        observeProfileUpdate()
    }

    @RequiresApi(Build.VERSION_CODES.P)
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
                selectedImageUri?.let { uri ->
                    compressAndUploadImage(uri)
                } ?: Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeStateList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateList.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        Log.d("datax", "observeStateList: ${response.data!!.data}")
                        Constants.setupDropdown(
                            binding.stateEditText,
                            response.data!!.data,
                            displayProperty = { state -> state.name },
                            context = requireContext()
                        ) { selectedState ->
                            binding.cityEditText.text= null
                            viewModel.allCity(selectedState.city)
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
                            response.data!!.data,
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

    @RequiresApi(Build.VERSION_CODES.P)
    private fun compressAndUploadImage(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)

                val compressedFile = File(requireContext().cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(compressedFile)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // Compress to 50% quality
                outputStream.flush()
                outputStream.close()

                withContext(Dispatchers.Main) {
                    userViewModel.fetchTokenAndUpdateProfile(
                        imageFile = compressedFile,
                        city = binding.cityEditText.text.toString().trim(),
                        state = binding.stateEditText.text.toString().trim(),
                        profession = binding.ProfessionEditText.text.toString().trim(),
                        maritalStatus = binding.meritalStatusEditText.text.toString().trim()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Image compression failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun openGallery() {
        pickImageLauncher.launch(IMAGE_MIME_TYPE)
    }

    private fun observeProfileUpdate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.profileUpdate.collectLatest { response ->
                    if (!isAdded || isRemoving) return@collectLatest

                    ResponceHelper.handleApiResponse(
                        response,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            requireActivity().finish()
                            this.cancel()
                        },
                        onError = {
                            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                        },
                        "profileUpdate"
                    )
                }
            }
        }
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
        selectedImageUri = imageUri
        profileImage.setImageURI(imageUri)
        profileImage.visibility = View.VISIBLE
        galleryImage.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
