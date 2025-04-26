package com.socialseller.bookpujari.adapter

import android.inputmethodservice.Keyboard
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialseller.bookpujari.apiResponce.catrgory.Data
import com.socialseller.bookpujari.databinding.RowCategoriesBinding
import com.socialseller.bookpujari.model.Category
import com.socialseller.clothcrew.utility.GlideHelper

class CategoryPagingAdapter : PagingDataAdapter<Data, CategoryPagingAdapter.CategoryViewHolder>(
    object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem
    }
) {
    inner class CategoryViewHolder(private val binding: RowCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Data) {
            binding.categoryName.text = item.name
            GlideHelper.loadImage(binding.categoryImage, item.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = RowCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}
