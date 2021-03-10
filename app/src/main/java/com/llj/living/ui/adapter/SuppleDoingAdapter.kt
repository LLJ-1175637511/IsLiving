package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.SuppleDoingBean
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.databinding.ItemSuppleDoingBinding
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.ui.activity.ActivitySuppleInfo

class SuppleDoingAdapter(private val vm:SupplementViewModel):BaseReloadAdapter<SuppleDoingBean>(DIFF_ITEM_CALLBACK) {

    companion object{
        private val DIFF_ITEM_CALLBACK= object :DiffUtil.ItemCallback<SuppleDoingBean>(){
            override fun areItemsTheSame(
                oldItem: SuppleDoingBean,
                newItem: SuppleDoingBean
            ): Boolean = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: SuppleDoingBean,
                newItem: SuppleDoingBean
            ): Boolean = oldItem.id== newItem.id

        }
    }

    override fun layoutId(): Int = R.layout.item_supple_doing

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemSuppleDoingBinding>(
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

    inner class SuppleDoingViewHolder(private val binding:ItemSuppleDoingBinding):RecyclerView.ViewHolder(binding.root){

        fun bindData(bean:SuppleDoingBean?){
            if (bean==null) return
            binding.apply {
                itemTittleSuppleDoing.text = bean.title
                tvIdNumSuppleDoing.text = bean.idCard
                tvNameSuppleDoing.text = bean.name
            }
            binding.btSupplementing.setOnClickListener { view ->
                view.context.also {
                    it.startActivity(Intent(it, ActivitySuppleInfo::class.java))
                }
            }
        }

    }
}