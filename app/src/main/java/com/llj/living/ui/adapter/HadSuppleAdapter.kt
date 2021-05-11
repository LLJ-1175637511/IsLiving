package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.database.OldManInfoHad
import com.llj.living.databinding.ItemMainHadBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.activity.ActivitySuppleInfo

class HadSuppleAdapter(private val vm: DatabaseVM) :
    BaseReloadAdapter<OldManInfoHad>(DIFF_CALLBACK) {

    override fun layoutId(): Int = R.layout.item_main_had

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainHadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            HadSuppleViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemReloadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            FooterViewHolder(binding)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == layoutId()) (holder as HadSuppleViewHolder).bindData(
            getItem(position)
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
//                vm.hadSuppleFactory.retryLoadData()
            }
        }
    }

    inner class HadSuppleViewHolder(private val binding: ItemMainHadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: OldManInfoHad?) {
            if (data == null) return
            binding.apply {
                tvName.text = data.name
                tvIdNum.text = data.idCard
                tvSex.text = data.sex
                btOperas.setOnClickListener {
                    it.context.startActivity(Intent(it.context, ActivitySuppleInfo::class.java))
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OldManInfoHad>() {
            override fun areItemsTheSame(
                oldItem: OldManInfoHad,
                newItem: OldManInfoHad
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: OldManInfoHad,
                newItem: OldManInfoHad
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}