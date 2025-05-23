package com.socialseller.bookpujari.UI.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.UI.home.HomeActivity
import com.socialseller.bookpujari.databinding.FragmentAddBalanceBinding
import com.socialseller.bookpujari.databinding.FragmentEditProfileBinding


class AddBalanceFragment : Fragment(R.layout.fragment_add_balance) {

    private var _binding: FragmentAddBalanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            proceedButton.setOnClickListener {
                findNavController().navigate(R.id.action_AddBalanceFragment_to_PaymentSuccessfulFragment)
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