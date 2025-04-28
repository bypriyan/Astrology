package com.socialseller.bookpujari.UI.home.chat

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// ChatPagerAdapter.kt
class ChatPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecentChatsFragment()
            1 -> NewChatFragment()
            else -> RecentChatsFragment()
        }
    }
}
