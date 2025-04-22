package com.socialseller.bookpujari.UI.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentOTPBinding
import com.socialseller.bookpujari.databinding.FragmentProfileDetailsBinding

class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Constants.setupDropdown(binding.meritalStatusEditText, listOf(
            "Single", "Married", "Widowed", "Divorced", "Separated", "Other"
        ), requireContext())

        Constants.setupDropdown(binding.stateEditText, listOf(
            "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana",
            "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
            "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
            "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
            "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Jammu and Kashmir", "Ladakh", "Lakshadweep", "Puducherry"
        ), requireContext())

        Constants.setupDropdown(binding.cityEditText, listOf(
            "Mumbai", "Delhi", "Bangalore", "Hyderabad", "Ahmedabad", "Chennai", "Kolkata", "Surat", "Pune",
            "Jaipur", "Lucknow", "Kanpur", "Nagpur", "Indore", "Thane", "Bhopal", "Visakhapatnam", "Pimpri-Chinchwad",
            "Patna", "Vadodara", "Ghaziabad", "Ludhiana", "Agra", "Nashik", "Faridabad", "Meerut", "Rajkot",
            "Kalyan-Dombivli", "Vasai-Virar", "Varanasi", "Srinagar", "Aurangabad", "Dhanbad", "Amritsar", "Navi Mumbai",
            "Allahabad", "Ranchi", "Howrah", "Coimbatore", "Jabalpur", "Gwalior", "Vijayawada", "Jodhpur", "Madurai",
            "Raipur", "Kota", "Chandigarh", "Guwahati", "Solapur", "Hubli-Dharwad", "Bareilly", "Moradabad", "Mysore"
        ), requireContext())

        binding.requestOtpButton.setOnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
