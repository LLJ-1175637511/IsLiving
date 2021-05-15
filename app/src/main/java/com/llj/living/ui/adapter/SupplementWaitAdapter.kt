package com.llj.living.ui.adapter

import android.content.Intent
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.data.bean.InfoByEntIdBean
import com.llj.living.databinding.ItemMainWaitBinding
import com.llj.living.ui.activity.ActivitySuppleDetails

class SupplementWaitAdapter :
    PagingDataAdapter<InfoByEntIdBean, SupplementWaitAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "SupplementWaitAdapter"

    inner class AdViewHolder(private val binding: ItemMainWaitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: InfoByEntIdBean) {
            binding.addonsByEntIdBean = bean
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivitySuppleDetails::class.java).apply {
                        putExtra(ActivitySuppleDetails.INTENT_ID_SUPPLE_FLAG, bean.id)
                        putExtra(ActivitySuppleDetails.INTENT_BEAN_SUPPLE_FLAG,bean)
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(
            inflateDataBinding<ItemMainWaitBinding>(
                parent,
                R.layout.item_main_wait
            )
        )
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<InfoByEntIdBean>() {
            override fun areItemsTheSame(oldItem: InfoByEntIdBean, newItem: InfoByEntIdBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: InfoByEntIdBean,
                newItem: InfoByEntIdBean
            ) = oldItem == newItem
        }
    }
}