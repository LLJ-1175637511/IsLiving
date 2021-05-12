package com.llj.living.ui.adapter

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.buildCoroutineDialog
import com.llj.living.custom.ext.commonLaunch
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.custom.ext.quickTokenRequest
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.data.bean.EntInfoBean
import com.llj.living.data.bean.NewsByIdBean
import com.llj.living.databinding.DialogWebviewBinding
import com.llj.living.databinding.ItemAdContentBinding
import com.llj.living.databinding.ItemMainDoingBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.MainFragmentVM
import com.llj.living.logic.vm.SupplementTestViewModel
import com.llj.living.net.repository.SystemRepository
import com.llj.living.ui.activity.ActivitySupplement
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class SupplementDoingTestAdapter :
    PagingDataAdapter<EntAddonsBean, SupplementDoingTestAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "SupplementDoingAdapter"

    inner class AdViewHolder(
        private val binding: ItemMainDoingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: EntAddonsBean) {
            binding.entAddons = bean
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    SuppleDoingAdapter.id = bean.id
                    LogUtils.d("SuppleDoingAdapter", "id:${SuppleDoingAdapter.id}")
                    it.startActivity(Intent(it, ActivitySupplement::class.java))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(
            inflateDataBinding<ItemMainDoingBinding>(
                parent,
                R.layout.item_main_doing
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

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntAddonsBean>() {
            override fun areItemsTheSame(oldItem: EntAddonsBean, newItem: EntAddonsBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: EntAddonsBean, newItem: EntAddonsBean) =
                oldItem == newItem
        }
    }
}