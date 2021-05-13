package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.data.database.OldManInfoWait
import com.llj.living.databinding.FragmentWaitSuppleBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.logic.vm.SupplementTestVM
import com.llj.living.ui.activity.ActivitySupplement
import com.llj.living.ui.adapter.SupplementDoingTestAdapter
import com.llj.living.ui.adapter.SupplementWaitTestAdapter
import com.llj.living.ui.adapter.WaitSuppleAdapter
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WaitSuppleFragment private constructor() : NavBaseFragment<FragmentWaitSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_supple

    private val supplementVm by activityViewModels<SupplementTestVM>()

    private val adapter by lazy { SupplementWaitTestAdapter() }

    private var addonsId: Int = -1

    override fun init() {
        getBinding().recyclerviewWaitSupple.adapter = adapter

        addonsId =
            requireActivity().intent.getIntExtra(SupplementDoingTestAdapter.SUPPLE_ID_FLAG, -1)

        getBinding().refreshWaitSupple.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                loadData()
            }
        }
        loadData()
    }

    private fun loadData() {
        if (addonsId == -1) {
            ToastUtils.toastShort("id错误 请返回重试")
            return
        }
        getBinding().refreshWaitSupple.apply {
            lifecycleScope.launch {
                isRefreshing = true
                supplementVm.getDoingData(addonsId).collectLatest {
                    LogUtils.d(TAG, "suc finished")
                    isRefreshing = false
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        private var instance: WaitSuppleFragment? = null
        fun getInstance() = instance ?: WaitSuppleFragment().also {
            instance = it
        }
    }

}