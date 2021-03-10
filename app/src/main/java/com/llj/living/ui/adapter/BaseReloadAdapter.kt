package com.llj.living.ui.adapter

import android.view.View
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.enums.NetStatus
import com.llj.living.databinding.ItemReloadBinding

abstract class BaseReloadAdapter<T>(diff:DiffUtil.ItemCallback<T>):PagedListAdapter<T,RecyclerView.ViewHolder>(diff) {

    private var netStatus = NetStatus.INIT

    private var hasFooter = false

    abstract fun layoutId():Int

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) R.layout.item_reload else layoutId()
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 1 else 0
    }

    fun updateLoadingUi(netStatus: NetStatus) {
        this.netStatus = netStatus
        if (netStatus == NetStatus.INIT) {
            hideLoadingView()
        }else{
            showLoadingView()
        }
    }

    private fun showLoadingView() {
        if (hasFooter) {
            notifyItemChanged(itemCount - 1)
        } else {
            hasFooter = true
            if (hasFooter) notifyItemInserted(itemCount - 1)
        }
    }

    private fun hideLoadingView() {
        if (hasFooter) notifyItemRemoved(itemCount - 1)
        hasFooter = false
    }

    inner class FooterViewHolder(private val binding: ItemReloadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData() {
            when (netStatus) {
                NetStatus.FAILED -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.itemLoadTips.text = "点击重试"
                    itemView.isClickable = true
                }
                NetStatus.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    itemView.isClickable = false
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.itemLoadTips.text = "正在加载"
                    itemView.isClickable = false
                }
            }
        }
    }
}