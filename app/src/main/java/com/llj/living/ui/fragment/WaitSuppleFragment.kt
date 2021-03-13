package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentWaitSuppleBinding
import com.llj.living.logic.vm.ActSuppleViewModel
import com.llj.living.ui.adapter.WaitSuppleAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WaitSuppleFragment private constructor(): NavBaseFragment<FragmentWaitSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_supple

    private val viewModel by activityViewModels<ActSuppleViewModel>()
    private val adapter by lazy { WaitSuppleAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewWaitSupple.adapter = adapter

        viewModel.mainCheckLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshWaitSupple.isRefreshing = false
        })

        viewModel.getWaitSuppleNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshWaitSupple.apply {
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
        viewModel.mainCheckLiveData.value?.dataSource?.invalidate()
    }

    companion object{
        private var instance:WaitSuppleFragment ?= null
        fun getInstance() = instance?:WaitSuppleFragment().also {
            instance = it
        }
    }

}