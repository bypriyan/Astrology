package com.socialseller.bookpujari.UI.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentEditProfileBinding
import com.socialseller.bookpujari.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            editProfile.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_EditProfileFragment)
            }
            addBanance.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_AddBalanceFragment)
            }
            transactions.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_TransactionFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}