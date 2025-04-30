package com.socialseller.bookpujari.UI.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.auth.AuthActivity
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentPanditDetailsBinding
import com.socialseller.bookpujari.databinding.FragmentSearchBinding
import com.socialseller.bookpujari.viewModel.PanditViewModel
import com.socialseller.bookpujari.viewModel.UserViewModel
import com.socialseller.clothcrew.utility.ResponceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class PanditDetailsFragment : Fragment(R.layout.fragment_pandit_details) {

    private var _binding: FragmentPanditDetailsBinding? = null
    private val binding get() = _binding!!
    val args: PanditDetailsFragmentArgs by navArgs()

    val panditViewModel: PanditViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPanditDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val panditId = args.panditId
        panditViewModel.getSinglePandit(panditId.toInt())
        observePanditDetails()

        binding.apply {

        }
    }

    private fun observePanditDetails() {
        lifecycleScope.launch {
            panditViewModel.singlePandit.collectLatest { response->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        Log.d("panditDetails", "observeUserDetail: ${response.data?.data}")
                    },
                    onError = { error->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    },
                    logTag = "panditDetails"
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? HomeActivity)?.hideBottomNavigation()
    }

    override fun onPause() {
        super.onPause()
        (activity as? HomeActivity)?.showBottomNavigation()
    }
}