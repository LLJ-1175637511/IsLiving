package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentSuppleFinishedBinding
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.ui.adapter.SuppleFinishedAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuppleFinishFragment private constructor():NavBaseFragment<FragmentSuppleFinishedBinding>() {

    override fun getLayoutId() = R.layout.fragment_supple_finished

    private val viewModel by activityViewModels<SupplementViewModel>()
    private val adapter by lazy { SuppleFinishedAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewSuppleFinished.adapter = adapter

        viewModel.finishedLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshSuppleFinished.isRefreshing = false
        })

        viewModel.getFinishedNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshSuppleFinished.apply {
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
        viewModel.finishedLiveData.value?.dataSource?.invalidate()
    }

    companion object{
        private var instance:SuppleFinishFragment ?= null
        fun getInstance() = instance?:SuppleFinishFragment()
    }
}