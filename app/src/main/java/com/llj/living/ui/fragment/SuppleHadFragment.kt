package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentHadSuppleBinding
import com.llj.living.logic.vm.SupplementPeopleVM
import com.llj.living.ui.adapter.SupplementDoingAdapter
import com.llj.living.ui.adapter.SupplementHadAdapter
import com.llj.living.utils.LogUtils
import com.llj.living.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SuppleHadFragment private constructor() : NavBaseFragment<FragmentHadSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_had_supple

    private val supplementVm by activityViewModels<SupplementPeopleVM>()

    private val adapter by lazy { SupplementHadAdapter() }

    private var addonsId: Int = -1

    override fun init() {
        getBinding().recyclerviewHadSupple.adapter = adapter

        addonsId =
            requireActivity().intent.getIntExtra(SupplementDoingAdapter.SUPPLE_ID_FLAG, -1)

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
        private var instance: SuppleHadFragment? = null
        fun getInstance() = instance ?: SuppleHadFragment().also {
            instance = it
        }
    }

}