package com.socialseller.bookpujari.UI.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentChatBinding
import com.socialseller.bookpujari.databinding.FragmentChattingBinding

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            chatOne.root.setOnClickListener {
                findNavController().navigate(R.id.action_chatFragment_to_ChattingFragment)
            }
            chatTwo.root.setOnClickListener {
                findNavController().navigate(R.id.action_chatFragment_to_ChattingFragment)
            }
            chatThree.root.setOnClickListener {
                findNavController().navigate(R.id.action_chatFragment_to_ChattingFragment)
            }
            chatFour.root.setOnClickListener {
                findNavController().navigate(R.id.action_chatFragment_to_ChattingFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}