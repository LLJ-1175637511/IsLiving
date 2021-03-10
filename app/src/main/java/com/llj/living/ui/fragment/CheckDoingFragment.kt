package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentCheckDoingBinding
import com.llj.living.logic.vm.CheckViewModel
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckDoingFragment : NavBaseFragment<FragmentCheckDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_check_doing

    private val viewModel by activityViewModels<CheckViewModel>()
    private val adapter by lazy { CheckDoingAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewCheckDoing.adapter = adapter

        viewModel.doingLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshCheckDoing.isRefreshing = false
        })

        viewModel.getDoingNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshCheckDoing.apply {
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
        viewModel.doingLiveData.value?.dataSource?.invalidate()
    }

}