package com.llj.living.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.databinding.FragmentNewMainBinding
import com.llj.living.logic.vm.MainFragmentVM
import com.llj.living.ui.adapter.MainAdAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : NavBaseFragment<FragmentNewMainBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_new_main

    private val adapter by lazy { MainAdAdapter() }

    private val mainVM by viewModels<MainFragmentVM>()

    override fun init() {
        getBinding().recyclerViewMain.adapter = adapter

        getBinding().refreshLayoutMain.apply {
            setOnRefreshListener {
                isRefreshing = true
                loadData()
                isRefreshing = false
            }
        }
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            LogUtils.d("MainFragmentTest","suc finished")
            mainVM.getData().collectLatest {
                LogUtils.d("MainFragmentTest","times")
                adapter.submitData(it)
            }
        }
    }

    override fun setLifeOwnerIsActivity(): Boolean = true

}