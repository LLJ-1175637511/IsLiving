package com.llj.living.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.database.SuppleFinished
import com.llj.living.databinding.ItemMainFinishedBinding
import com.llj.living.databinding.ItemReloadBinding

class SuppleFinishedAdapter():BaseReloadAdapter<SuppleFinished>(DIFF_CALLBACK) {

    override fun layoutId(): Int = R.layout.item_main_finished

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainFinishedBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            SuppleFinishedViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemReloadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            FooterViewHolder(binding)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == layoutId()) (holder as SuppleFinishedViewHolder).bindData(
            getItem(position)
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
//                vm.finishedFactory.retryLoadData()
            }
        }
    }

    inner class SuppleFinishedViewHolder(private val binding: ItemMainFinishedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: SuppleFinished?) {
            if (data==null) return
            binding.apply {
                tvTittle.text = data.title
                tvItemWait.text = data.waitDealWith.toString()
                tvItemHad.text = data.hadDealWith.toString()
                tvStartDate.text = data.startTime
                tvEndDate.text = data.endTime
                itemView.context.resources.let {
                    tvHadTypeStr.text = it.getString(R.string.had_supple_str)
                    tvWaitTypeStr.text = it.getString(R.string.wait_supple_str)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SuppleFinished>() {
            override fun areItemsTheSame(
                oldItem: SuppleFinished,
                newItem: SuppleFinished
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: SuppleFinished,
                newItem: SuppleFinished
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}