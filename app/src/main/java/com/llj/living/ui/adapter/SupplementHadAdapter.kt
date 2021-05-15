package com.llj.living.ui.adapter

import android.content.Intent
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.data.bean.InfoByEntIdBean
import com.llj.living.databinding.ItemMainHadBinding
import com.llj.living.ui.activity.ActivitySuppleDetails

class SupplementHadAdapter :
    PagingDataAdapter<InfoByEntIdBean, SupplementHadAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "SupplementHadAdapter"

    inner class AdViewHolder(private val binding: ItemMainHadBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
            inflateDataBinding<ItemMainHadBinding>(parent, R.layout.item_main_had)
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