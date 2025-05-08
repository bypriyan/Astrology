package com.socialseller.bookpujari.UI.home.chat


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.details.PanditDetailsFragment
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.adapter.PanditPagingAdapter
import com.socialseller.bookpujari.databinding.FragmentChatBinding
import com.socialseller.bookpujari.databinding.FragmentNewChatBinding
import com.socialseller.bookpujari.viewModel.PanditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewChatFragment : Fragment(R.layout.fragment_new_chat) {

    private var _binding: FragmentNewChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PanditViewModel by viewModels()
    private lateinit var panditAdapter: PanditPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchPandits()
    }

    private fun setupRecyclerView() {
        panditAdapter = PanditPagingAdapter() { id ->
            val action =
                NewChatFragmentDirections.actionNewChatFragmentToPanditDetailsFragment(panditId = id)
            findNavController().navigate(action)
        }
        binding.recyclearPandits.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = panditAdapter
        }
    }

    private fun fetchPandits() {
        lifecycleScope.launch {
            viewModel.getPandits(city = "Raipur", state = "Chhattisgarh")
                .collectLatest { pagingData ->
                    panditAdapter.submitData(pagingData)
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