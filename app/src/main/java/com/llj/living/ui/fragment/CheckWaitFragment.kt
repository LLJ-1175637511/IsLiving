package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.getSP
import com.llj.living.custom.ext.save
import com.llj.living.data.const.Const
import com.llj.living.databinding.FragmentWaitCheckBinding
import com.llj.living.logic.vm.CheckPeopleVM
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.ui.adapter.CheckWaitTestAdapter
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckWaitFragment : NavBaseFragment<FragmentWaitCheckBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_check

    private val checkVm by activityViewModels<CheckPeopleVM>()

    private var reputId: Int = -1

    private val adapter by lazy { CheckWaitTestAdapter() }

    override fun init() {

        reputId =
            requireActivity().intent.getIntExtra(CheckDoingAdapter.CHECK_ID_FLAG, -1)

        if (reputId == -1) {
            ToastUtils.toastShort("id错误 请返回重试")
            return
        }

        requireContext().getSP(Const.SPCheck).save {
            putInt(Const.SPCheckReputId,reputId)
        }

        getBinding().recyclerviewWaitCheck.adapter = adapter

        getBinding().refreshWaitCheck.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                isRefreshing = true
                loadData()
            }
        }

        loadData()
    }

    private fun loadData() {
        getBinding().refreshWaitCheck.apply {
            lifecycleScope.launch {
                checkVm.getDoingData(reputId).collectLatest {
                    LogUtils.d(TAG, "suc finished")
                    isRefreshing = false
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        private var instance: CheckWaitFragment? = null
        fun getInstance() = instance ?: CheckWaitFragment().also {
            instance = it
        }
    }

}