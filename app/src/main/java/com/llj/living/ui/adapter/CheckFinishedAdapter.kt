package com.llj.living.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.CheckFinishedBean
import com.llj.living.databinding.ItemCheckFinishedBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.CheckViewModel

class CheckFinishedAdapter(private val vm: CheckViewModel):BaseReloadAdapter<CheckFinishedBean>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemCheckFinishedBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            CheckFinishedViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemReloadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            FooterViewHolder(binding)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == layoutId()) (holder as CheckFinishedViewHolder).bindData(
            getItem(position)
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
                vm.finishedFactory.retryLoadData()
            }
        }
    }

    override fun layoutId(): Int = R.layout.item_check_finished

    inner class CheckFinishedViewHolder(private val binding: ItemCheckFinishedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: CheckFinishedBean?) {
            if (data == null) {
                binding.apply {
                    tvNameCheckFinished.text =
                        itemView.context.resources.getString(R.string.default_name)
                    itemView.context.resources.apply {
                        val inyardTime =
                            "${getString(R.string.inyard_time)}${getString(R.string.default_name)}"
                        tvInyardTimeCheckFinished.text = inyardTime
                        val uNum =
                            "${getString(R.string.user_num)}${getString(R.string.default_name)}"
                        tvNumCheckFinished.text = uNum
                        tvAgeCheckFinished.text = "00"
                    }
                }
            } else {
                binding.apply {
                    val num = "编号：${data.num}"
                    val age = "年龄：${data.age}"
                    tvNameCheckFinished.text = data.uName
                    tvInyardTimeCheckFinished.text = data.inyardTime
                    tvNumCheckFinished.text = num
                    tvAgeCheckFinished.text = age
                }
            }
//            binding.ivImgCheckFinished.setImageResource(R.mipmap.logo)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckFinishedBean>() {
            override fun areItemsTheSame(
                oldItem: CheckFinishedBean,
                newItem: CheckFinishedBean
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CheckFinishedBean,
                newItem: CheckFinishedBean
            ): Boolean {
                return oldItem.age == newItem.age
            }

        }
    }
}