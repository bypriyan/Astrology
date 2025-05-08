package com.socialseller.bookpujari.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.socialseller.bookpujari.adapter.CategoryPagingAdapter.CategoryViewHolder
import com.socialseller.bookpujari.apiResponce.pandit.singlePandit.ServiceType
import com.socialseller.bookpujari.databinding.RowPanditServiceCategoryBinding
import com.socialseller.clothcrew.utility.GlideHelper

class PanditServiceCategoryAdapter(
    private val onItemClick: (ServiceType) -> Unit
) : ListAdapter<ServiceType, PanditServiceCategoryAdapter.PanditServiceCategoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanditServiceCategoryViewHolder {
        val binding = RowPanditServiceCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PanditServiceCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PanditServiceCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PanditServiceCategoryViewHolder(
        private val binding: RowPanditServiceCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ServiceType) = with(binding) {
            categoryName.text = item.name.toString()
            priceTv.text = buildString {
                append("₹")
                append(item.price.toString())
            }
            GlideHelper.loadImage(categoryImage, item.image)

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ServiceType>() {
            override fun areItemsTheSame(oldItem: ServiceType, newItem: ServiceType): Boolean {
                return oldItem.categoryId == newItem.categoryId
            }

            override fun areContentsTheSame(oldItem: ServiceType, newItem: ServiceType): Boolean {
                return oldItem == newItem
            }
        }
    }
}