package com.llj.living.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.data.bean.EntCheckBean
import com.llj.living.databinding.ItemMainFinishedBinding

class CheckFinishedAdapter :
    PagingDataAdapter<EntCheckBean, CheckFinishedAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "CheckFinishedAdapter"

    inner class AdViewHolder(
        private val binding: ItemMainFinishedBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: EntCheckBean) {
            bean.apply {
                val entBean = EntAddonsBean(
                    check_name,
                    id,
                    people_count,
                    people_check_count,
                    start_at,
                    end_at
                )
                binding.entAddons = entBean
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(
            inflateDataBinding<ItemMainFinishedBinding>(
                parent,
                R.layout.item_main_finished
            )
        )
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntCheckBean>() {
            override fun areItemsTheSame(oldItem: EntCheckBean, newItem: EntCheckBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: EntCheckBean, newItem: EntCheckBean) =
                oldItem == newItem
        }
    }
}