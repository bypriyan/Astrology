package com.socialseller.bookpujari.UI.auth.forgot

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
import com.socialseller.bookpujari.databinding.FragmentOTPForgotPasswordBinding

class OTPForgotPasswordFragment : Fragment(R.layout.fragment_o_t_p_forgot_password) {

    private var _binding: FragmentOTPForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOTPForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            requestOTPBtn.setOnClickListener {
                findNavController().navigate(R.id.action_ForgotOTPFragment_to_NewPasswordFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}