package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.database.CheckDoing
import com.llj.living.databinding.ItemMainDoingBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.activity.ActivityCheck
import com.llj.living.utils.LogUtils

class CheckDoingAdapter(private val vm: DatabaseVM) :
    BaseReloadAdapter<CheckDoing>(DIFF_CALLBACK) {

    override fun layoutId() = R.layout.item_main_doing

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainDoingBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            binding.ivImg.setImageResource(R.drawable.ic_baseline_how_to_reg_24)
            CheckDoingViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemReloadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == layoutId()) (holder as CheckDoingViewHolder).bindData(
            getItem(position)
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
//                vm.doingFactory.retryLoadData()
            }
        }
    }

    inner class CheckDoingViewHolder(private val binding: ItemMainDoingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(bean: CheckDoing?) {
            if (bean == null) return
            binding.apply {
                tvTittle.text = bean.title
                tvStartTime.text = bean.startTime
                tvEndTime.text = bean.endTime
                tvItemWait.text = bean.waitDealWith.toString()
                tvItemHad.text = bean.hadDealWith.toString()
                btOperas.setOnClickListener { view ->
                    view.context.let {
                        id = bean.id
                        LogUtils.d("CheckDoingAdapter", "id:${bean.id}")
                        it.startActivity(Intent(it, ActivityCheck::class.java))
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckDoing>() {
            override fun areItemsTheSame(
                oldItem: CheckDoing,
                newItem: CheckDoing
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CheckDoing,
                newItem: CheckDoing
            ): Boolean = oldItem == newItem
        }
        var id = -1
    }
}