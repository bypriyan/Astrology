package com.socialseller.bookpujari.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialseller.bookpujari.apiResponce.pandit.Pandit
import com.socialseller.bookpujari.databinding.RowPanditsBinding
import com.socialseller.clothcrew.utility.GlideHelper

class PanditPagingAdapter(
    private val onItemClick: (String) -> Unit
) : PagingDataAdapter<Pandit, PanditPagingAdapter.PanditViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanditViewHolder {
        val binding = RowPanditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PanditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PanditViewHolder, position: Int) {
        Log.d("PagingData", "Item Loaded at position: $position")
        val pandit = getItem(position)
        if (pandit != null) {
            holder.bind(pandit)
        }
    }

    inner class PanditViewHolder(private val binding: RowPanditsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pandit: Pandit) {
            binding.apply {
                GlideHelper.loadImage(binding.profileImage, pandit.profile_image)
                panditName.text = pandit.username
                binding.reviewRating.text = "${pandit.averageRating}(324)"
                binding.chatPricePerMin.text = "${pandit.chatRatePerMinute}/min"
                itemView.setOnClickListener {
                    onItemClick(pandit.id.toString())
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pandit>() {
            override fun areItemsTheSame(oldItem: Pandit, newItem: Pandit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Pandit, newItem: Pandit): Boolean {
                return oldItem == newItem
            }
        }
    }
}