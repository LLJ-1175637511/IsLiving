package com.llj.living.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.CheckDoingBean
import com.llj.living.databinding.ItemCheckDoingBinding

class CheckDoingAdapter :
    PagedListAdapter<CheckDoingBean, CheckDoingAdapter.CheckDoingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckDoingViewHolder {
        val binding = DataBindingUtil.inflate<ItemCheckDoingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_check_doing, parent, false
        )
        return CheckDoingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckDoingViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null) {
            holder.binding.apply {
                itemTittleCheckDoing.text = holder.itemView.context.resources.getString(R.string.default_name)
                holder.itemView.context.resources.apply {
                    val startText = "${getString(R.string.start_time)}${getString(R.string.default_name)}"
                    tvStartTimeCheckDoing.text = startText
                    val releaseText = "${getString(R.string.release_time)}${getString(R.string.default_name)}"
                    tvReleaseTimeCheckDoing.text = releaseText
                }
            }
        } else {
            holder.binding.apply {
                itemTittleCheckDoing.text = bean.title
                tvStartTimeCheckDoing.text = bean.startTime
                tvReleaseTimeCheckDoing.text = bean.releaseTime
            }
        }
        holder.binding.ivImgCheckDoing.setImageResource(R.mipmap.logo)
    }

    inner class CheckDoingViewHolder(val binding: ItemCheckDoingBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckDoingBean>() {
            override fun areItemsTheSame(
                oldItem: CheckDoingBean,
                newItem: CheckDoingBean
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: CheckDoingBean,
                newItem: CheckDoingBean
            ): Boolean {
                val rt = oldItem.releaseTime == newItem.releaseTime
                val st = oldItem.startTime == newItem.startTime
                return rt == st
            }

        }
    }
}