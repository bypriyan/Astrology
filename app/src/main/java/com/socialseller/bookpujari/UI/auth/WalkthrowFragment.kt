package com.socialseller.bookpujari.UI.auth

import android.content.Context
import android.content.Intent
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
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentWalkthrowBinding
import com.socialseller.bookpujari.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class WalkthrowFragment : Fragment(R.layout.fragment_walkthrow) {

    private var _binding: FragmentWalkthrowBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var dataStore: DataStoreManager
    private var hasNavigated = false
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWalkthrowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthStatus()

        binding.apply {
            requestOTPBtn.setOnClickListener {
                findNavController().navigate(R.id.action_WalkthrowFragment_to_loginFragment)
            }
            previousBtn.setOnClickListener {
                findNavController().navigate(R.id.action_WalkthrowFragment_to_registrationFragment)
            }
        }
    }

    private fun observeAuthStatus() {
        viewModel.authStatus.observe(viewLifecycleOwner) { (token, profession) ->
            if (hasNavigated) return@observe
            if (!token.isNullOrEmpty()) {
                hasNavigated = true
                if (!profession.isNullOrEmpty() && profession != "null") {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                } else {
                    findNavController().navigate(R.id.action_WalkthrowFragment_to_profileDetailsFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}