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
import com.llj.living.ui.activity.ActivityCheckDetail
import com.llj.living.ui.activity.ActivityVideotape

class CheckWaitTestAdapter :
    PagingDataAdapter<InfoByEntIdBean, CheckWaitTestAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "CheckWaitAdapter"

    inner class AdViewHolder(private val binding: ItemMainWaitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: InfoByEntIdBean) {
            binding.addonsByEntIdBean = bean
            binding.root.setOnClickListener {
                it.context.startActivity(Intent(it.context, ActivityCheckDetail::class.java).apply {
                    putExtra(ActivityCheckDetail.INTENT_ID_CHECK_FLAG, bean.id)
                    putExtra(ActivityCheckDetail.INTENT_BEAN_CHECK_FLAG,bean)
                })
            }
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivityVideotape::class.java).apply {
                        putExtra(ActivityVideotape.INTENT_ID_CHECK_FLAG, bean.id)
                        putExtra(ActivityVideotape.INTENT_BEAN_CHECK_FLAG,bean)
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
            ).apply {
                btOperas.text = parent.context.resources.getString(R.string.video_check)
            }
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