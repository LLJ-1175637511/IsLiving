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
import com.llj.living.ui.activity.ActivityCheckDetail
import com.llj.living.ui.activity.ActivitySuppleDetails
import com.llj.living.ui.activity.ActivityVideotape

class CheckHadTestAdapter :
    PagingDataAdapter<InfoByEntIdBean, CheckHadTestAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "CheckHadAdapter"

    inner class AdViewHolder(private val binding: ItemMainHadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: InfoByEntIdBean) {
            binding.addonsByEntIdBean = bean
            binding.root.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivityCheckDetail::class.java).apply {
                        putExtra(ActivitySuppleDetails.INTENT_ID_SUPPLE_FLAG, bean.id)
                        putExtra(ActivitySuppleDetails.INTENT_BEAN_SUPPLE_FLAG,bean)
                    })
                }
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
            inflateDataBinding<ItemMainHadBinding>(parent, R.layout.item_main_had).apply {
                btOperas.text = parent.context.resources.getString(R.string.re_check)
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