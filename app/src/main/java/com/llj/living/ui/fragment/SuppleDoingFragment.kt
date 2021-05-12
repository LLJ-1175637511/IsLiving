package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.custom.ext.baseObserver
import com.llj.living.data.database.SuppleDoing
import com.llj.living.databinding.FragmentSuppleDoingBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.logic.vm.SupplementTestViewModel
import com.llj.living.ui.adapter.SuppleDoingAdapter
import com.llj.living.ui.adapter.SupplementDoingTestAdapter
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SuppleDoingFragment private constructor() : NavBaseFragment<FragmentSuppleDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_supple_doing

    //    private val viewModel by activityViewModels<SupplementViewModel>()
//    private val dbViewModel by activityViewModels<DatabaseVM>()
//    private val dbViewModel by activityViewModels<DatabaseVM>()

    private val suppleDoingVM by activityViewModels<SupplementTestViewModel>()

//    private val adapter by lazy { SuppleDoingAdapter() }
    private val adapterTest by lazy { SupplementDoingTestAdapter(suppleDoingVM) }

//    private lateinit var pagedListLives: LiveData<PagedList<SuppleDoing>>

    override fun init() {
        getBinding().recyclerviewSuppleDoing.adapter = adapterTest

        /* viewModel.doingLiveData.observe(this, Observer {
             adapter.submitList(it)
             getBinding().refreshSuppleDoing.isRefreshing = false
         })*/

        /* viewModel.getDoingNS().observe(viewLifecycleOwner, Observer {
             adapter.updateLoadingUi(it)
             LogUtils.d("status", it.name)
         })*/

       /* pagedListLives = LivePagedListBuilder(dbViewModel.getSuppleDoingListLD(), 5).build()

        pagedListLives.observe(requireActivity(), Observer { data ->
            data?.let {
                adapter.submitList(it)
            }
        })*/
/* MyApplication.suppleDoingList.baseObserver(this) {
            adapter.submitList(null)
            refreshData()
        }*/

        getBinding().refreshSuppleDoing.apply {
            setColorSchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                isRefreshing = true
                loadData()
                isRefreshing = false
            }
        }
        loadData()
    }

    /**
     * 获取补录进行列表
     */
    private fun loadData(){
        return
        lifecycleScope.launch {
            suppleDoingVM.getDoingData().collectLatest {
                LogUtils.d(TAG,"suc finished")
                suppleDoingVM.getDoingData().collectLatest {
                    LogUtils.d(TAG,"times")
                    adapterTest.submitData(it)
                }
            }
        }
    }

    companion object {
        private var instance: SuppleDoingFragment? = null
        fun getInstance() = instance ?: SuppleDoingFragment()
    }
}