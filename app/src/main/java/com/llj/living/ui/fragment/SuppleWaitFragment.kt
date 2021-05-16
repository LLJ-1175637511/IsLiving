package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import com.llj.living.R
import com.llj.living.custom.ext.getSP
import com.llj.living.custom.ext.save
import com.llj.living.data.const.Const
import com.llj.living.databinding.FragmentWaitSuppleBinding
import com.llj.living.logic.vm.SupplementPeopleVM
import com.llj.living.ui.adapter.SupplementDoingAdapter
import com.llj.living.ui.adapter.SupplementWaitAdapter
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SuppleWaitFragment private constructor() : NavBaseFragment<FragmentWaitSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_supple

    private val supplementVm by activityViewModels<SupplementPeopleVM>()

    private var addonsId: Int = -1

    private val adapter by lazy { SupplementWaitAdapter() }

    override fun init() {

        addonsId =
            requireActivity().intent.getIntExtra(SupplementDoingAdapter.SUPPLE_ID_FLAG, -1)

        if (addonsId == -1) {
            ToastUtils.toastShort("id错误 请返回重试")
            return
        }

        requireContext().getSP(Const.SPAddons).save {
            putInt(Const.SPAddonsReputId,addonsId)
        }

        getBinding().recyclerviewWaitSupple.adapter = adapter

        getBinding().refreshWaitSupple.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                loadData()
            }
        }

        loadData()
    }

    private fun loadData() {

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

        private var instance: SuppleWaitFragment? = null
        fun getInstance() = instance ?: SuppleWaitFragment().also {
            instance = it
        }
    }

}