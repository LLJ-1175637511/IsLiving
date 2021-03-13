package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.databinding.ItemMainWaitBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.ActSuppleViewModel
import com.llj.living.ui.activity.ActivitySuppleInfo

class WaitSuppleAdapter(private val vm: ActSuppleViewModel):BaseReloadAdapter<SecondFragmentBean>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainWaitBinding>(
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
                vm.waitSuppleFactory.retryLoadData()
            }
        }
    }

    override fun layoutId(): Int = R.layout.item_main_wait

    inner class HadSuppleViewHolder(private val binding: ItemMainWaitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: SecondFragmentBean?) {
            if (data==null) return
            binding.apply {
                tvName.text = data.uName
                tvIdNum.text = data.idCard
                tvSex.text = data.sex
                btOperas.setOnClickListener {
                    it.context.startActivity(Intent(it.context,ActivitySuppleInfo::class.java))
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SecondFragmentBean>() {
            override fun areItemsTheSame(
                oldItem: SecondFragmentBean,
                newItem: SecondFragmentBean
            ): Boolean = oldItem.uName == newItem.uName

            override fun areContentsTheSame(
                oldItem: SecondFragmentBean,
                newItem: SecondFragmentBean
            ): Boolean {
                return oldItem.idCard == newItem.idCard
            }

        }
    }
}