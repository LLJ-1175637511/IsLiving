package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.data.database.OldManInfoWait
import com.llj.living.databinding.FragmentWaitSuppleBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.activity.ActivitySupplement
import com.llj.living.ui.adapter.WaitSuppleAdapter
import com.llj.living.utils.LogUtils

class WaitSuppleFragment private constructor() : NavBaseFragment<FragmentWaitSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_supple

    private val dbViewModel by activityViewModels<DatabaseVM>()
    private val adapter by lazy { WaitSuppleAdapter() }
    private lateinit var pagedListLives: LiveData<PagedList<OldManInfoWait>>
    override fun init() {
        getBinding().recyclerviewWaitSupple.adapter = adapter

        pagedListLives = LivePagedListBuilder(dbViewModel.getOldManInfoLD(), 3).build()

        pagedListLives.observe(requireActivity(), Observer { data ->
            data?.let { pData ->
                val waitCount = ActivitySupplement.waitSuppleCount
                val tempList = mutableListOf<OldManInfoWait>()
                pData.forEachIndexed { index, bean ->
                    LogUtils.d("WaitSuppleFragment", "bean:${bean}")
                    if (index in 1..waitCount) {
                        bean?.let {
                            tempList.add(it)
                        }
                    }
                }
                adapter.submitList(tempList)
            }
        })

        /*    viewModel.waitSuppleLiveData.observe(this, Observer {
                adapter.submitList(it)
                getBinding().refreshWaitSupple.isRefreshing = false
            })

            viewModel.getWaitSuppleNS().observe(this, Observer {
                adapter.updateLoadingUi(it)
                LogUtils.d("status", it.name)
            })*/

        /*      MyApplication.suppleWaitList.observe(this, Observer {
                  LogUtils.d("WaitSuppleFragment", "refreshData")
                  lifecycleScope.launch {
                      delay(200)
                      adapter.submitList(null)
                      refreshData()
                  }
              })*/


        /*getBinding().refreshWaitSupple.apply {
            setColor  SchemeResources(R.color.qq_blue) //设置显示颜色
            setOnRefreshListener {
                isRefreshing = true
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(500)
                    //重置数据
                    refreshData()
                }
            }
        }*/
    }


    /*  private fun refreshData() {
          viewModel.waitSuppleLiveData.value?.dataSource?.invalidate()
      }*/

    companion object {
        private var instance: WaitSuppleFragment? = null
        fun getInstance() = instance ?: WaitSuppleFragment().also {
            instance = it
        }
    }

}