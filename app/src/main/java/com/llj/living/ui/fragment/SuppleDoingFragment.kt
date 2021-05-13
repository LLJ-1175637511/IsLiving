package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.data.database.SuppleDoing
import com.llj.living.databinding.FragmentSuppleDoingBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.logic.vm.SupplementTestViewModel
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.ui.adapter.SuppleDoingAdapter
import com.llj.living.ui.adapter.SupplementDoingTestAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SuppleDoingFragment private constructor() : NavBaseFragment<FragmentSuppleDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_supple_doing

    private val suppleDoingVM by activityViewModels<SupplementTestViewModel>()

    private val adapterTest by lazy { SupplementDoingTestAdapter() }

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