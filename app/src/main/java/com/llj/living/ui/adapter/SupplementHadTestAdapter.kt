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

class SupplementHadTestAdapter :
    PagingDataAdapter<AddonsByEntIdBean, SupplementHadTestAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "SupplementHadAdapter"

    inner class AdViewHolder(private val binding: ItemMainHadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(bean: AddonsByEntIdBean) {
            binding.addonsByEntIdBean = bean
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivitySuppleInfo::class.java).apply {
                        putExtra(SUPPLE_ID_HAD_FLAG, bean.id)
                        putExtra(SUPPLE_BEAN_HAD_FLAG,bean)
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

        const val SUPPLE_ID_HAD_FLAG = "supple_id_had_flag"
        const val SUPPLE_BEAN_HAD_FLAG = "supple_bean_had_flag"

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