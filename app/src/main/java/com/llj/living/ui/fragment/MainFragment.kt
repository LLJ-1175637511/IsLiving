package com.llj.living.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.custom.ext.loadView
import com.llj.living.databinding.FragmentNewMainBinding
import com.llj.living.logic.vm.MainFragmentVM
import com.llj.living.ui.adapter.MainAdAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : NavBaseFragment<FragmentNewMainBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_new_main

    private val mainVM by viewModels<MainFragmentVM>()

    private val adapter by lazy { MainAdAdapter(mainVM) }

    override fun init() {
        getBinding().recyclerViewMain.adapter = adapter

        getBinding().refreshLayoutMain.apply {
            setOnRefreshListener {
                isRefreshing = true
                loadData()
                isRefreshing = false
            }
        }

        mainVM.adsLiveData.baseObserver(this){
            if (!it.isNullOrEmpty()){
                loadView(it[0].img,getBinding().ivAdArea)
            }
        }

        mainVM.loadAdsData()
        loadData()
    }

    /**
     * 获取新闻榜单数据
     */
    private fun loadData() {
        lifecycleScope.launch {
            LogUtils.d(TAG,"suc finished")
            mainVM.getData().collectLatest {
                LogUtils.d(TAG,"times")
                adapter.submitData(it)
            }
        }
    }

    override fun setLifeOwnerIsActivity(): Boolean = true

}