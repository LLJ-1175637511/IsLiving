package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentHadCheckBinding
import com.llj.living.logic.vm.ActCheckViewModel
import com.llj.living.ui.adapter.HadCheckAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HadCheckFragment : NavBaseFragment<FragmentHadCheckBinding>() {

    override fun getLayoutId() = R.layout.fragment_had_check

    private val viewModel by activityViewModels<ActCheckViewModel>()
    private val adapter by lazy { HadCheckAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewHadCheck.adapter = adapter

        viewModel.hadCheckLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshHadCheck.isRefreshing = false
        })

        viewModel.getHadCheckNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshHadCheck.apply {
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
        viewModel.hadCheckLiveData.value?.dataSource?.invalidate()
    }

    companion object{
        private var instance:HadCheckFragment ?= null
        fun getInstance() = instance?:HadCheckFragment().also {
            instance = it
        }
    }

}