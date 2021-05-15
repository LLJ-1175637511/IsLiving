package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentSuppleFinishedBinding
import com.llj.living.logic.vm.SupplementEntVM
import com.llj.living.ui.adapter.SupplementFinishedAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SuppleFinishFragment private constructor() : NavBaseFragment<FragmentSuppleFinishedBinding>() {

    override fun getLayoutId() = R.layout.fragment_supple_finished

    private val suppleFinishedVM by activityViewModels<SupplementEntVM>()

    private val adapterTest by lazy { SupplementFinishedAdapter() }

    override fun init() {
        getBinding().recyclerviewSuppleFinished.adapter = adapterTest

        getBinding().refreshSuppleFinished.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                loadData()
            }
        }

        loadData()
    }

    private fun loadData(){
        getBinding().refreshSuppleFinished.apply {
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

    companion object {
        private var instance: SuppleFinishFragment? = null
        fun getInstance() = instance ?: SuppleFinishFragment()
    }
}