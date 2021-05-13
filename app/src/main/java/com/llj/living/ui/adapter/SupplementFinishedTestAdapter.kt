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
import com.llj.living.databinding.*
import com.llj.living.logic.vm.MainFragmentVM
import com.llj.living.logic.vm.SupplementTestViewModel
import com.llj.living.net.repository.SystemRepository
import com.llj.living.ui.activity.ActivitySupplement
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class SupplementFinishedTestAdapter :
    PagingDataAdapter<EntAddonsBean, SupplementFinishedTestAdapter.AdViewHolder>(DIFF_CALLBACK) {

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