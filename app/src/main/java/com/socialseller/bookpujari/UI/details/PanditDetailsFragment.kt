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
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bypriyan.bustrackingsystem.utility.Constants
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.auth.AuthActivity
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.adapter.CategoryPagingAdapter
import com.socialseller.bookpujari.adapter.PanditServiceCategoryAdapter
import com.socialseller.bookpujari.apiResponce.pandit.singlePandit.Data
import com.socialseller.bookpujari.apiResponce.pandit.singlePandit.RatingSummary
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

    private val args: PanditDetailsFragmentArgs by navArgs()
    private val panditViewModel: PanditViewModel by viewModels()
    private lateinit var panditServiceCategoryAdapter: PanditServiceCategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPanditDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        panditServiceCategoryAdapter = PanditServiceCategoryAdapter(){

        }
        binding.recyclearCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = panditServiceCategoryAdapter
        }


        panditViewModel.getSinglePandit(args.panditId.toInt())
        observePanditDetails()
        setStaticRatings()
    }

    private fun observePanditDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            panditViewModel.singlePandit.collectLatest { response ->
                ResponceHelper.handleApiResponse(
                    response,
                    onSuccess = {
                        val pandit = response.data?.data
                        updateUi(pandit)
                        // ✅ Submit list to adapter
                        panditServiceCategoryAdapter.submitList(pandit?.service_type)
                                },
                    onError = { showToast(it) },
                    logTag = "panditDetails"
                )
            }
        }
    }

    private fun updateUi(pandit: Data?) = with(binding) {
        pandit?.let {
            panditNameActionBar.text = it.username
            panditDetails.apply {
                panditName.text = it.username
                poojas.text = Constants.concatenateServiceNamesWithPipe(it.service_type)
                languages.text = "Languages: ${Constants.concatenateList(it.languagesSpoken, ", ")}"
                ratingAndReview.text = "${it.averageRating} (${it.ratingCount})"
                chatCharge.text = "₹${it.chatRatePerMinute}/min"
            }

            about.text = it.about
            ratingLayout.apply {
                avgRating.text = "${it.averageRating}/5"
                ratingBar.rating = it.averageRating.toFloat()
                ratingCount.text = "${it.ratingCount} Ratings"

                setProgressRatings(it.rating_summary)
            }
        }
    }

    private fun setStaticRatings() = with(binding.ratingLayout) {
        oneRating.ratingNumber.text = "1"
        twoRating.ratingNumber.text = "2"
        threeRating.ratingNumber.text = "3"
        fourRating.ratingNumber.text = "4"
        fiveRating.ratingNumber.text = "5"
    }

    private fun setProgressRatings(summary: RatingSummary) = with(binding.ratingLayout) {
        oneRating.progressBarRating.progress = summary.`1`.toIntOrNull() ?: 0
        twoRating.progressBarRating.progress = summary.`2`.toIntOrNull() ?: 0
        threeRating.progressBarRating.progress = summary.`3`.toIntOrNull() ?: 0
        fourRating.progressBarRating.progress = summary.`4`.toIntOrNull() ?: 0
        fiveRating.progressBarRating.progress = summary.`5`.toIntOrNull() ?: 0
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
