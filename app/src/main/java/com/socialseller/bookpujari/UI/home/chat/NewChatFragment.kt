package com.socialseller.bookpujari.UI.home.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socialseller.bookpujari.R
import com.socialseller.bookpujari.databinding.FragmentChatBinding
import com.socialseller.bookpujari.databinding.FragmentNewChatBinding

class NewChatFragment : Fragment(R.layout.fragment_new_chat) {

    private var _binding: FragmentNewChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentNewChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_chat, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}