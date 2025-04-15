package com.socialseller.bookpujari.UI.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentLoginBinding
import com.socialseller.bookpujari.databinding.FragmentOTPBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            requestOTPBtn.setOnClickListener {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
            }
            registerLayout.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            forgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_UserNamesFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}