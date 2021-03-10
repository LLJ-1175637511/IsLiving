package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.SuppleFinishedBean
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.databinding.ItemSuppleFinishedBinding
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.ui.activity.ActivitySuppleInfo

class SuppleFinishedAdapter(private val vm: SupplementViewModel):BaseReloadAdapter<SuppleFinishedBean>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemSuppleFinishedBinding>(
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

    override fun layoutId(): Int = R.layout.item_supple_finished

    inner class SuppleFinishedViewHolder(private val binding: ItemSuppleFinishedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: SuppleFinishedBean?) {
            if (data == null) return
                binding.apply {
                    tvNameSuppleFinished.text = data.uName
                    tvIdNumSuppleFinished.text = data.idCard
                    tvTimeSuppleFinished.text = data.time
                }
            binding.btSupplementing.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivitySuppleInfo::class.java))
                }
            }
//            binding.ivImgCheckFinished.setImageResource(R.mipmap.logo)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SuppleFinishedBean>() {
            override fun areItemsTheSame(
                oldItem: SuppleFinishedBean,
                newItem: SuppleFinishedBean
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SuppleFinishedBean,
                newItem: SuppleFinishedBean
            ): Boolean {
                return oldItem.idCard == newItem.idCard
            }

        }
    }
}