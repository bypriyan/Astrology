package com.socialseller.bookpujari.UI.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentHomeBinding
import com.socialseller.bookpujari.databinding.FragmentLoginBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            notificationBtn.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_notificationFragment)
            }
            searchViewClick.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }

            panditRow1.root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_panditDetailsFragment)
            }
            panditRow2.root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_panditDetailsFragment)
            }
            panditRow3.root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_panditDetailsFragment)
            }

            catOne.root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_PujaListyFragment)
            }
            catTwo.root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_PujaListyFragment)
            }
            catThree.root.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_PujaListyFragment)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}