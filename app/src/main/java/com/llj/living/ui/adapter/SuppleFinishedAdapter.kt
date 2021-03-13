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
import com.llj.living.logic.vm.SupplementViewModel

class SuppleFinishedAdapter(private val vm: SupplementViewModel):BaseReloadAdapter<MainFragmentBean>(DIFF_CALLBACK) {

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
                vm.finishedFactory.retryLoadData()
            }
        }
    }

    inner class SuppleFinishedViewHolder(private val binding: ItemMainFinishedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: MainFragmentBean?) {
            if (data==null) return
            binding.apply {
                tvTittle.text = data.title
                itemView.context.resources.let {
                    tvHadTypeStr.text = it.getString(R.string.had_supple_str)
                    tvWaitTypeStr.text = it.getString(R.string.wait_supple_str)
                }
            }
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