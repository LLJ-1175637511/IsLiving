package com.llj.living.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.custom.ext.buildCoroutineDialog
import com.llj.living.custom.ext.commonLaunch
import com.llj.living.custom.ext.inflateDataBinding
import com.llj.living.custom.ext.quickTokenRequest
import com.llj.living.data.bean.EntInfoBean
import com.llj.living.data.bean.NewsByIdBean
import com.llj.living.databinding.DialogWebviewBinding
import com.llj.living.databinding.ItemAdContentBinding
import com.llj.living.logic.vm.MainFragmentVM
import com.llj.living.net.repository.SystemRepository
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class MainAdAdapter(private val mainVM: MainFragmentVM) :
    PagingDataAdapter<EntInfoBean, MainAdAdapter.AdViewHolder>(DIFF_CALLBACK) {

    private val TAG = "MainAdAdapter"

    inner class AdViewHolder(
        private val binding: ItemAdContentBinding,
        private val parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: EntInfoBean) {
            binding.tvTitleAd.text = item.title
            binding.tvLikesCount.text = (0..10).random().toString()
            binding.root.setOnClickListener {
                LogUtils.d(TAG, "click")
                buildNewsDialog(parent, item.news_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        return AdViewHolder(
            inflateDataBinding<ItemAdContentBinding>(
                parent,
                R.layout.item_ad_content
            ), parent
        )
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    fun buildNewsDialog(
        parent: ViewGroup,
        newsId: Int
    ) {
        val addView = inflateDataBinding<DialogWebviewBinding>(
            parent,
            R.layout.dialog_webview
        )
        mainVM.commonLaunch(Dispatchers.Main) {
            buildCoroutineDialog(parent, addView.root, "新闻") {
                delay(500)
                val newsBean = quickTokenRequest<NewsByIdBean> { token ->
                    SystemRepository.getNewsByIdRequest(token, newsId)
                }
                newsBean ?: throw Exception("data load failed")
                addView.apply {
                    webView.settings.apply {
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        setSupportZoom(true)
                    }
                    webView.loadData(newsBean.content, WEB_FORMAT, null)
                    webView.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {

        const val WEB_FORMAT = "text/html"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntInfoBean>() {
            override fun areItemsTheSame(oldItem: EntInfoBean, newItem: EntInfoBean) =
                oldItem.news_id == newItem.news_id

            override fun areContentsTheSame(oldItem: EntInfoBean, newItem: EntInfoBean) =
                oldItem == newItem
        }
    }
}