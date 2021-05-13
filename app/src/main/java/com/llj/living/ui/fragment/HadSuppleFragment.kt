package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.data.database.OldManInfoHad
import com.llj.living.databinding.FragmentHadSuppleBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.logic.vm.SupplementTestVM
import com.llj.living.ui.adapter.HadSuppleAdapter
import com.llj.living.ui.adapter.SupplementDoingTestAdapter
import com.llj.living.ui.adapter.SupplementHadTestAdapter
import com.llj.living.ui.adapter.SupplementWaitTestAdapter
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HadSuppleFragment private constructor() : NavBaseFragment<FragmentHadSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_had_supple

    private val supplementVm by activityViewModels<SupplementTestVM>()

    private val adapter by lazy { SupplementHadTestAdapter() }

    private var addonsId: Int = -1

    override fun init() {
        getBinding().recyclerviewHadSupple.adapter = adapter

        addonsId =
            requireActivity().intent.getIntExtra(SupplementDoingTestAdapter.SUPPLE_ID_FLAG, -1)

        getBinding().refreshHadSupple.apply {
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
        getBinding().refreshHadSupple.apply {
            lifecycleScope.launch {
                isRefreshing = true
                supplementVm.getFinishedData(addonsId).collectLatest {
                    LogUtils.d(TAG, "suc finished")
                    isRefreshing = false
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        private var instance: HadSuppleFragment? = null
        fun getInstance() = instance ?: HadSuppleFragment().also {
            instance = it
        }
    }

}