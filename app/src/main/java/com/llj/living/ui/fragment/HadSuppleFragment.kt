package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentHadSuppleBinding
import com.llj.living.logic.vm.ActSuppleViewModel
import com.llj.living.ui.adapter.HadSuppleAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HadSuppleFragment private constructor(): NavBaseFragment<FragmentHadSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_had_supple

    private val viewModel by activityViewModels<ActSuppleViewModel>()
    private val adapter by lazy { HadSuppleAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewHadSupple.adapter = adapter

        viewModel.hadSuppleLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshHadSupple.isRefreshing = false
        })

        viewModel.getHadSuppleNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshHadSupple.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                isRefreshing = true
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(500)
                    //重置数据
                    refreshData()
                }
            }
        }
    }

    private fun refreshData() {
        viewModel.hadSuppleLiveData.value?.dataSource?.invalidate()
    }

    companion object{
        private var instance:HadSuppleFragment ?= null
        fun getInstance() = instance?:HadSuppleFragment().also {
            instance = it
        }
    }

}