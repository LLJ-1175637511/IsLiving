package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.databinding.ItemMainDoingBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.CheckViewModel
import com.llj.living.ui.activity.ActivityCheck

class CheckDoingAdapter(private val vm: CheckViewModel) :
    BaseReloadAdapter<MainFragmentBean>(DIFF_CALLBACK) {

    override fun layoutId() = R.layout.item_main_doing

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainDoingBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
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
            getItem(
                position
            )
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
                vm.doingFactory.retryLoadData()
            }
        }
    }

    inner class CheckDoingViewHolder(private val binding: ItemMainDoingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(bean: MainFragmentBean?) {
            if (bean == null) return
            binding.apply {
                tvTittle.text = bean.title
                btOperas.setOnClickListener { view ->
                    view.context.also {
                        it.startActivity(Intent(it, ActivityCheck::class.java))
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MainFragmentBean>() {
            override fun areItemsTheSame(
                oldItem: MainFragmentBean,
                newItem: MainFragmentBean
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: MainFragmentBean,
                newItem: MainFragmentBean
            ): Boolean = oldItem.id == newItem.id
        }
    }
}