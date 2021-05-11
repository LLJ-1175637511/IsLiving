package com.llj.living.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.data.bean.EntInfoBean
import com.llj.living.databinding.ItemAdContentBinding

class MainAdAdapter : PagingDataAdapter<EntInfoBean, MainAdAdapter.AdViewHolder>(DIFF_CALLBACK) {

    inner class AdViewHolder(private val binding: ItemAdContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: EntInfoBean) {
            binding.tvTitleAd.text = item.title
            binding.tvLikesCount.text = (0..10).random().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(inflateDataBinding<ItemAdContentBinding>(parent,R.layout.item_ad_content))
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntInfoBean>() {
            override fun areItemsTheSame(oldItem: EntInfoBean, newItem: EntInfoBean) =
                oldItem.news_id == newItem.news_id

            override fun areContentsTheSame(oldItem: EntInfoBean, newItem: EntInfoBean) =
                oldItem == newItem
        }
    }
}