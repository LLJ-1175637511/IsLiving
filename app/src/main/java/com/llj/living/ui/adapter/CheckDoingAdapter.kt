package com.llj.living.ui.adapter

import android.content.Intent
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.data.bean.EntCheckBean
import com.llj.living.databinding.ItemCheckDoingBinding
import com.llj.living.ui.activity.ActivityCheck

class CheckDoingAdapter :
    PagingDataAdapter<EntCheckBean, CheckDoingAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "CheckDoingAdapter"

    inner class AdViewHolder(private val binding: ItemCheckDoingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: EntCheckBean) {
            binding.entAddons = bean
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivityCheck::class.java).apply {
                        putExtra(CHECK_ID_FLAG, bean.id)
                        putExtra(CHECK_BEAN_FLAG, bean)
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(
            inflateDataBinding<ItemCheckDoingBinding>(
                parent,
                R.layout.item_check_doing
            )
        )
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    companion object {

        const val CHECK_ID_FLAG = "check_id_flag"
        const val CHECK_BEAN_FLAG = "check_bean_flag"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntCheckBean>() {
            override fun areItemsTheSame(oldItem: EntCheckBean, newItem: EntCheckBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: EntCheckBean, newItem: EntCheckBean) =
                oldItem == newItem
        }
    }
}