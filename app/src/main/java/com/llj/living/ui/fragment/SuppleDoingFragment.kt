package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentSuppleDoingBinding
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.ui.adapter.SuppleDoingAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuppleDoingFragment private constructor():NavBaseFragment<FragmentSuppleDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_supple_doing

    private val viewModel by activityViewModels<SupplementViewModel>()
    private val adapter by lazy { SuppleDoingAdapter(viewModel) }

    override fun init() {
        getBinding().recyclerviewSuppleDoing.adapter = adapter

        viewModel.doingLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshSuppleDoing.isRefreshing = false
        })

        viewModel.getDoingNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshSuppleDoing.apply {
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

    companion object{
        private var instance:SuppleDoingFragment ?= null
        fun getInstance() = instance?:SuppleDoingFragment()
    }
}