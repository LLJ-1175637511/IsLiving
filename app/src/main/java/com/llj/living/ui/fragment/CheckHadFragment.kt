package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentHadSuppleBinding
import com.llj.living.logic.vm.CheckPeopleVM
import com.llj.living.ui.adapter.CheckDoingAdapter
import com.llj.living.ui.adapter.CheckHadAdapter
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckHadFragment private constructor() : NavBaseFragment<FragmentHadSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_had_supple

    private val checkVm by activityViewModels<CheckPeopleVM>()

    private val adapter by lazy { CheckHadAdapter() }

    private var checkId: Int = -1

    override fun init() {
        getBinding().recyclerviewHadSupple.adapter = adapter

        checkId =
            requireActivity().intent.getIntExtra(CheckDoingAdapter.CHECK_ID_FLAG, -1)

        getBinding().refreshHadSupple.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                loadData()
            }
        }

        loadData()

    }

    private fun loadData() {
        if (checkId == -1) {
            ToastUtils.toastShort("id错误 请返回重试")
            return
        }
        getBinding().refreshHadSupple.apply {
            lifecycleScope.launch {
                isRefreshing = true
                checkVm.getFinishedData(checkId).collectLatest {
                    LogUtils.d(TAG, "suc finished")
                    isRefreshing = false
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        private var instance: CheckHadFragment? = null
        fun getInstance() = instance ?: CheckHadFragment().also {
            instance = it
        }
    }

}