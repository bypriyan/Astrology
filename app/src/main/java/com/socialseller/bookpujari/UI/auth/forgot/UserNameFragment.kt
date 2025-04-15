package com.socialseller.bookpujari.UI.auth.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentOTPForgotPasswordBinding
import com.socialseller.bookpujari.databinding.FragmentUserNameBinding


class UserNameFragment : Fragment(R.layout.fragment_user_name) {

    private var _binding: FragmentUserNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            requestOTPBtn.setOnClickListener {
                findNavController().navigate(R.id.action_UserNamesFragment_to_ForgotOTPFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}