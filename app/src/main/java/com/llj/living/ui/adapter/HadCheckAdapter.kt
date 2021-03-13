package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.databinding.ItemMainHadBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.logic.vm.ActCheckViewModel
import com.llj.living.ui.activity.ActivityCheckDetail

class HadCheckAdapter(private val vm: ActCheckViewModel):BaseReloadAdapter<SecondFragmentBean>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainHadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            HadCheckViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemReloadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            FooterViewHolder(binding)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == layoutId()) (holder as HadCheckViewHolder).bindData(
            getItem(position)
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
                vm.hadCheckFactory.retryLoadData()
            }
        }
    }

    override fun layoutId(): Int = R.layout.item_main_had

    inner class HadCheckViewHolder(private val binding: ItemMainHadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: SecondFragmentBean?) {
            if (data == null) return
            binding.apply {
                tvName.text = data.uName
                tvIdNum.text = data.idCard
                tvSex.text = data.sex
                btOperas.text = itemView.context.resources.getString(R.string.re_check)
                btOperas.setOnClickListener {
                    it.context.startActivity(Intent(it.context, ActivityCheckDetail::class.java))
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
                return oldItem.id == newItem.id
            }

        }
    }
}