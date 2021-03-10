package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.CheckDoingBean
import com.llj.living.databinding.ItemCheckDoingBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.CheckViewModel
import com.llj.living.ui.activity.ActivityCheckDetail

class CheckDoingAdapter(private val vm: CheckViewModel) :
    BaseReloadAdapter<CheckDoingBean>(DIFF_CALLBACK) {

    override fun layoutId() = R.layout.item_check_doing

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemCheckDoingBinding>(
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
        )
        else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
                vm.doingFactory.retryLoadData()
            }
        }
    }

    inner class CheckDoingViewHolder(val binding: ItemCheckDoingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(bean: CheckDoingBean?) {
            if (bean == null) {
                binding.apply {
                    itemTittleCheckDoing.text =
                        itemView.context.resources.getString(R.string.default_name)
                    itemView.context.resources.apply {
                        val startText =
                            "${getString(R.string.start_time)}${getString(R.string.default_name)}"
                        tvStartTimeCheckDoing.text = startText
                        val releaseText =
                            "${getString(R.string.release_time)}${getString(R.string.default_name)}"
                        tvReleaseTimeCheckDoing.text = releaseText
                    }
                }
            } else {
                binding.apply {
                    itemTittleCheckDoing.text = bean.title
                    tvStartTimeCheckDoing.text = bean.startTime
                    tvReleaseTimeCheckDoing.text = bean.releaseTime
                }
                binding.btChecking.setOnClickListener { view ->
                    view.context.also {
                        it.startActivity(Intent(it,ActivityCheckDetail::class.java))
                    }
                }
            }
//            binding.ivImgCheckDoing.setImageResource(R.mipmap.logo)

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckDoingBean>() {
            override fun areItemsTheSame(
                oldItem: CheckDoingBean,
                newItem: CheckDoingBean
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: CheckDoingBean,
                newItem: CheckDoingBean
            ): Boolean = oldItem.id == newItem.id

        }
    }
}