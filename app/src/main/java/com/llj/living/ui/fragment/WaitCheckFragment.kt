package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentWaitCheckBinding
import com.llj.living.logic.vm.ActCheckViewModel
import com.llj.living.ui.adapter.WaitCheckAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WaitCheckFragment : NavBaseFragment<FragmentWaitCheckBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_check

    private val viewModel by activityViewModels<ActCheckViewModel>()
    private val adapter by lazy { WaitCheckAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewWaitCheck.adapter = adapter

        viewModel.waitCheckLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshWaitCheck.isRefreshing = false
        })

        viewModel.getWaitCheckNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshWaitCheck.apply {
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
        viewModel.waitCheckLiveData.value?.dataSource?.invalidate()
    }

    companion object{
        private var instance:WaitCheckFragment ?= null
        fun getInstance() = instance?:WaitCheckFragment().also {
            instance = it
        }
    }

}