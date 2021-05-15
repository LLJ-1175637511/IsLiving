package com.llj.living.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.databinding.ItemMainFinishedBinding

class SupplementFinishedAdapter :
    PagingDataAdapter<EntAddonsBean, SupplementFinishedAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "SupplementFinishedAdapter"

    inner class AdViewHolder(
        private val binding: ItemMainFinishedBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: EntAddonsBean) {
            binding.entAddons = bean
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(
            inflateDataBinding<ItemMainFinishedBinding>(
                parent,
                R.layout.item_main_finished
            ).apply {
                tvHadTypeStr.text = root.context.getString(R.string.had_supple_str)
                tvWaitTypeStr.text = root.context.getString(R.string.wait_supple_str)
            }
        )
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntAddonsBean>() {
            override fun areItemsTheSame(oldItem: EntAddonsBean, newItem: EntAddonsBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: EntAddonsBean, newItem: EntAddonsBean) =
                oldItem == newItem
        }
    }
}