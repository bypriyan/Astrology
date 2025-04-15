package com.socialseller.bookpujari.UI.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentCategoryBinding
import com.socialseller.bookpujari.databinding.FragmentPujaListBinding


class PujaListFragment : Fragment(R.layout.fragment_puja_list) {

    private var _binding: FragmentPujaListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPujaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            catOne.root.setOnClickListener {
                findNavController().navigate(R.id.action_PujaListyFragment_to_CategoryFragment)
            }
            catTwo.root.setOnClickListener {
                findNavController().navigate(R.id.action_PujaListyFragment_to_CategoryFragment)
            }
            catThree.root.setOnClickListener {
                findNavController().navigate(R.id.action_PujaListyFragment_to_CategoryFragment)
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