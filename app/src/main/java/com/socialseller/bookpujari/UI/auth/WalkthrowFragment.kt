package com.socialseller.bookpujari.UI.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentWalkthrowBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class WalkthrowFragment : Fragment(R.layout.fragment_walkthrow) {

    private var _binding: FragmentWalkthrowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWalkthrowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            requestOTPBtn.setOnClickListener {
                findNavController().navigate(R.id.action_WalkthrowFragment_to_loginFragment)
            }
            previousBtn.setOnClickListener {
                findNavController().navigate(R.id.action_WalkthrowFragment_to_registrationFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}