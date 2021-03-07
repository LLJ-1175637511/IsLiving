package com.llj.living.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.CheckFinishedBean
import com.llj.living.databinding.ItemCheckFinishedBinding

class CheckFinishedAdapter :
    PagedListAdapter<CheckFinishedBean, CheckFinishedAdapter.CheckFinishedViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckFinishedViewHolder {
        val binding = DataBindingUtil.inflate<ItemCheckFinishedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_check_finished, parent, false
        )
        return CheckFinishedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckFinishedViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null) {
            holder.binding.apply {
                tvNameCheckFinished.text = holder.itemView.context.resources.getString(R.string.default_name)
                holder.itemView.context.resources.apply {
                    val inyardTime = "${getString(R.string.inyard_time)}${getString(R.string.default_name)}"
                    tvInyardTimeCheckFinished.text = inyardTime
                    val uNum = "${getString(R.string.user_num)}${getString(R.string.default_name)}"
                    tvNumCheckFinished.text = uNum
                    tvAgeCheckFinished.text = "00"
                }
            }
        } else {
            holder.binding.apply {
                tvNameCheckFinished.text = bean.uName
                tvInyardTimeCheckFinished.text = bean.inyardTime
                tvNumCheckFinished.text = bean.num.toString()
                tvAgeCheckFinished.text = bean.age.toString()
            }
        }
        holder.binding.ivImgCheckFinished.setImageResource(R.mipmap.logo)
    }

    inner class CheckFinishedViewHolder(val binding: ItemCheckFinishedBinding) :
        RecyclerView.ViewHolder(binding.root)

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
                val num = oldItem.num == newItem.num
                val uName = oldItem.uName == newItem.uName
                return num == uName
            }

        }
    }
}