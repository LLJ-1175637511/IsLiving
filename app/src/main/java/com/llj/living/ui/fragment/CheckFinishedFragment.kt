package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentCheckFinishedBinding
import com.llj.living.logic.vm.CheckEntVM
import com.llj.living.ui.adapter.CheckFinishedAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckFinishedFragment : NavBaseFragment<FragmentCheckFinishedBinding>() {

    override fun getLayoutId() = R.layout.fragment_check_finished

    private val suppleFinishedVM by activityViewModels<CheckEntVM>()

    private val adapterTest by lazy { CheckFinishedAdapter() }

    override fun init() {

        getBinding().recyclerviewCheckFinished.adapter = adapterTest

        getBinding().refreshCheckFinished.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                loadData()
            }
        }

        loadData()
    }

    private fun loadData(){
        getBinding().refreshCheckFinished.apply {
            lifecycleScope.launch {
                isRefreshing = true
                suppleFinishedVM.getFinishedData().collectLatest {
                    LogUtils.d(TAG,"suc finished")
                    isRefreshing = false
                    adapterTest.submitData(it)
                }
            }
        }
    }

}