package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentCheckFinishedBinding
import com.llj.living.logic.vm.CheckViewModel
import com.llj.living.ui.adapter.CheckFinishedAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckFinishedFragment : NavBaseFragment<FragmentCheckFinishedBinding>() {

    override fun getLayoutId() = R.layout.fragment_check_finished

    private val viewModel by activityViewModels<CheckViewModel>()
    private val adapter by lazy { CheckFinishedAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewCheckFinished.adapter = adapter

        viewModel.finishedLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshCheckFinished.isRefreshing = false
        })

        viewModel.getFinishedNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshCheckFinished.apply {
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

}