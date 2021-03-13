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
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.ui.activity.ActivitySupplement

class SuppleDoingAdapter(private val vm:SupplementViewModel):BaseReloadAdapter<MainFragmentBean>(DIFF_ITEM_CALLBACK) {

    override fun layoutId(): Int = R.layout.item_main_doing

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainDoingBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            SuppleDoingViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemReloadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == layoutId()) (holder as SuppleDoingViewHolder).bindData(
            getItem(position)
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
                vm.doingFactory.retryLoadData()
            }
        }
    }

    inner class SuppleDoingViewHolder(private val binding:ItemMainDoingBinding):RecyclerView.ViewHolder(binding.root){

        fun bindData(bean:MainFragmentBean?){
            if (bean==null) return
            binding.apply {
                tvTittle.text = bean.title
                itemView.context.resources.let {
                    tvHadTypeStr.text = it.getString(R.string.had_supple_str)
                    tvWaitTypeStr.text = it.getString(R.string.wait_supple_str)
                    btOperas.text = it.getString(R.string.supplementing)
                }
            }
            binding.btOperas.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivitySupplement::class.java))
                }
            }
        }

    }

    companion object{
        private val DIFF_ITEM_CALLBACK= object :DiffUtil.ItemCallback<MainFragmentBean>(){
            override fun areItemsTheSame(
                oldItem: MainFragmentBean,
                newItem: MainFragmentBean
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: MainFragmentBean,
                newItem: MainFragmentBean
            ): Boolean = oldItem.id== newItem.id

        }
    }
}