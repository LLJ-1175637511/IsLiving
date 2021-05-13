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
import com.llj.living.data.bean.AddonsByEntIdBean
import com.llj.living.data.bean.EntAddonsBean
import com.llj.living.data.bean.EntInfoBean
import com.llj.living.data.bean.NewsByIdBean
import com.llj.living.databinding.*
import com.llj.living.logic.vm.MainFragmentVM
import com.llj.living.logic.vm.SupplementTestViewModel
import com.llj.living.net.repository.SystemRepository
import com.llj.living.ui.activity.ActivitySuppleInfo
import com.llj.living.ui.activity.ActivitySupplement
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class SupplementWaitTestAdapter :
    PagingDataAdapter<AddonsByEntIdBean, SupplementWaitTestAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "SupplementWaitAdapter"

    inner class AdViewHolder(private val binding: ItemMainWaitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: AddonsByEntIdBean) {
            binding.addonsByEntIdBean = bean
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivitySuppleInfo::class.java).apply {
                        putExtra(SUPPLE_ID_WAIT_FLAG, bean.id)
                        putExtra(SUPPLE_BEAN_WAIT_FLAG, bean)
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

        const val SUPPLE_ID_WAIT_FLAG = "supple_id_wait_flag"
        const val SUPPLE_BEAN_WAIT_FLAG = "supple_bean_wait_flag"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AddonsByEntIdBean>() {
            override fun areItemsTheSame(oldItem: AddonsByEntIdBean, newItem: AddonsByEntIdBean) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: AddonsByEntIdBean,
                newItem: AddonsByEntIdBean
            ) = oldItem == newItem
        }
    }
}