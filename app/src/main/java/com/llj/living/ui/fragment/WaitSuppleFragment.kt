package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.getSP
import com.llj.living.custom.ext.save
import com.llj.living.data.const.Const
import com.llj.living.databinding.FragmentWaitSuppleBinding
import com.llj.living.logic.vm.SupplementTestVM
import com.llj.living.ui.adapter.SupplementDoingTestAdapter
import com.llj.living.ui.adapter.SupplementWaitTestAdapter
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WaitSuppleFragment private constructor() : NavBaseFragment<FragmentWaitSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_supple

    private val supplementVm by activityViewModels<SupplementTestVM>()

    private var addonsId: Int = -1

    private val adapter by lazy { SupplementWaitTestAdapter() }

    override fun init() {

        addonsId =
            requireActivity().intent.getIntExtra(SupplementDoingTestAdapter.SUPPLE_ID_FLAG, -1)

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

        private var instance: WaitSuppleFragment? = null
        fun getInstance() = instance ?: WaitSuppleFragment().also {
            instance = it
        }
    }

}