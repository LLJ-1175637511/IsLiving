package com.llj.living.ui.adapter

import android.content.Intent
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.databinding.ItemSuppleDoingBinding
import com.llj.living.ui.activity.ActivitySupplement

class SupplementDoingAdapter :
    PagingDataAdapter<EntAddonsBean, SupplementDoingAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "SupplementDoingAdapter"

    inner class AdViewHolder(
        private val binding: ItemSuppleDoingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: EntAddonsBean) {
            binding.entAddons = bean
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivitySupplement::class.java).apply {
                        putExtra(SUPPLE_ID_FLAG, bean.id)
                        putExtra(SUPPLE_BEAN_FLAG, bean)
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(
            inflateDataBinding<ItemSuppleDoingBinding>(
                parent,
                R.layout.item_supple_doing
            ).apply {
                root.context.resources.let {
                    tvHadTypeStr.text = it.getString(R.string.had_supple_str)
                    tvWaitTypeStr.text = it.getString(R.string.wait_supple_str)
                    btOperas.text = it.getString(R.string.supplementing)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    companion object {

        const val SUPPLE_ID_FLAG = "supple_id_flag"
        const val SUPPLE_BEAN_FLAG = "supple_bean_flag"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntAddonsBean>() {
            override fun areItemsTheSame(oldItem: EntAddonsBean, newItem: EntAddonsBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: EntAddonsBean, newItem: EntAddonsBean) =
                oldItem == newItem
        }
    }
}