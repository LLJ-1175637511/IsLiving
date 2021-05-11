package com.llj.living.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.database.OldManInfoWait
import com.llj.living.databinding.ItemMainWaitBinding
import com.llj.living.databinding.ItemReloadBinding
import com.llj.living.ui.activity.ActivityCheckDetail
import com.llj.living.ui.activity.ActivityVideotape

class WaitCheckAdapter : BaseReloadAdapter<OldManInfoWait>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == layoutId()) {
            val binding = DataBindingUtil.inflate<ItemMainWaitBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            WaitCheckViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemReloadBinding>(
                LayoutInflater.from(parent.context),
                viewType, parent, false
            )
            FooterViewHolder(binding)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == layoutId()) (holder as WaitCheckViewHolder).bindData(
            getItem(position)
        ) else (holder as BaseReloadAdapter<*>.FooterViewHolder).bindData().also {
            holder.itemView.setOnClickListener {
//                vm.waitCheckFactory.retryLoadData()
            }
        }
    }

    override fun layoutId(): Int = R.layout.item_main_wait

    inner class WaitCheckViewHolder(private val binding: ItemMainWaitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: OldManInfoWait?) {
            if (data == null) return
            itemView.setOnClickListener {
                id = data.id
                it.context.startActivity(Intent(it.context, ActivityCheckDetail::class.java))
            }
            binding.apply {
                tvName.text = data.name
                tvIdNum.text = data.idCard
                tvSex.text = data.sex
                btOperas.text = itemView.context.resources.getString(R.string.video_check)
                btOperas.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            ActivityVideotape::class.java
                        ).apply {
                            id = data.id
                            putExtra(VIDEO_PATH_ID, data.idCard)
                        })
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OldManInfoWait>() {
            override fun areItemsTheSame(
                oldItem: OldManInfoWait,
                newItem: OldManInfoWait
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: OldManInfoWait,
                newItem: OldManInfoWait
            ): Boolean {
                return oldItem == newItem
            }
        }
        const val VIDEO_PATH_ID = "video_path_id"
        var id = -1
    }
}