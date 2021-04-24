package com.llj.living.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.AdBean
import com.llj.living.databinding.ItemAdContentBinding

class MainAdAdapter : ListAdapter<AdBean, MainAdAdapter.AdViewHolder>(DIFF_CALLBACK) {

    inner class AdViewHolder(private val binding: ItemAdContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: AdBean) {
            binding.tvTitleAd.text = item.titleName
            binding.tvMainAddr.text = item.addr
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val binding = DataBindingUtil.inflate<ItemAdContentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_ad_content,
            parent,
            false
        )
        return AdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AdBean>() {
            override fun areItemsTheSame(oldItem: AdBean, newItem: AdBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AdBean, newItem: AdBean) =
                oldItem.titleName == newItem.titleName
        }
    }
}