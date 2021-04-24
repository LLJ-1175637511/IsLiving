package com.llj.living.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.databinding.ItemMainFinishedBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.CheckViewModel

class CheckFinishedAdapter(private val vm: CheckViewModel) :
    BaseReloadAdapter<MainFragmentBean>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainFinishedBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            binding.ivImg.setImageResource(R.drawable.ic_baseline_how_to_reg_24_orange)
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

    override fun layoutId(): Int = R.layout.item_main_finished

    inner class CheckFinishedViewHolder(private val binding: ItemMainFinishedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: MainFragmentBean?) {
            if (data == null) return
            binding.apply {
                tvTittle.text = data.title
                tvItemWait.text = data.waitDealWith.toString()
                tvItemHad.text = data.hadDealWith.toString()
                tvStartDate.text = data.startTime
                tvEndDate.text = data.endTime
                tvState.text = "已审核"
            }
//            binding.ivImgCheckFinished.setImageResource(R.mipmap.logo)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MainFragmentBean>() {
            override fun areItemsTheSame(
                oldItem: MainFragmentBean,
                newItem: MainFragmentBean
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: MainFragmentBean,
                newItem: MainFragmentBean
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}