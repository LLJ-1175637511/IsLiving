package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentSuppleDoingBinding
import com.llj.living.logic.vm.SupplementEntVM
import com.llj.living.ui.adapter.SupplementDoingAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SuppleDoingFragment private constructor() : NavBaseFragment<FragmentSuppleDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_supple_doing

    private val suppleDoingVM by activityViewModels<SupplementEntVM>()

    private val adapterTest by lazy { SupplementDoingAdapter() }

    override fun init() {
        getBinding().recyclerviewSuppleDoing.adapter = adapterTest

        getBinding().refreshSuppleDoing.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                loadData()
            }
        }

        loadData()
    }

    /**
     * 获取补录进行列表
     */
    private fun loadData(){
        getBinding().refreshSuppleDoing.apply {
            lifecycleScope.launch {
                isRefreshing = true
                suppleDoingVM.getDoingData().collectLatest {
                    LogUtils.d(TAG,"suc finished")
                    isRefreshing = false
                    adapterTest.submitData(it)
                }
            }
        }
    }

    companion object {
        private var instance: SuppleDoingFragment? = null
        fun getInstance() = instance ?: SuppleDoingFragment()
    }
}