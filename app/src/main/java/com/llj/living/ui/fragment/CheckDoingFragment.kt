package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.custom.ext.loadView
import com.llj.living.data.database.CheckDoing
import com.llj.living.databinding.FragmentCheckDoingBinding
import com.llj.living.logic.vm.CheckTestViewModel
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.ui.adapter.CheckDoingTestAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckDoingFragment : NavBaseFragment<FragmentCheckDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_check_doing

    private val checkVM by activityViewModels<CheckTestViewModel>()
    private val adapter by lazy { CheckDoingTestAdapter() }

    override fun init() {
        getBinding().recyclerviewCheckDoing.adapter = adapter

        getBinding().refreshCheckDoing.apply {
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
        /*getBinding().refreshCheckDoing.apply {
            lifecycleScope.launch {
                isRefreshing = true
                checkVM.getDoingData().collectLatest {
                    LogUtils.d(TAG,"suc finished")
                    isRefreshing = false
                    adapter.submitData(it)
                }
            }
        }*/
    }

}