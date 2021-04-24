package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.data.database.OldManInfoWait
import com.llj.living.databinding.FragmentWaitCheckBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.activity.ActivityCheck
import com.llj.living.ui.adapter.WaitCheckAdapter
import com.llj.living.utils.LogUtils

class WaitCheckFragment : NavBaseFragment<FragmentWaitCheckBinding>() {

    override fun getLayoutId() = R.layout.fragment_wait_check

    //    private val viewModel by activityViewModels<ActCheckViewModel>()
    private val adapter by lazy { WaitCheckAdapter() }

    private lateinit var pagedListLives: LiveData<PagedList<OldManInfoWait>>
    private val dbViewModel by activityViewModels<DatabaseVM>()

    override fun init() {
        getBinding().recyclerviewWaitCheck.adapter = adapter

        pagedListLives = LivePagedListBuilder(dbViewModel.getOldManInfoLD(), 3).build()

        pagedListLives.observe(requireActivity(), Observer { data ->
            data?.let { pData ->
                LogUtils.d("WaitCheckFragment", "waitSuppleCount:${ActivityCheck.waitSuppleCount}")
                val waitCount = ActivityCheck.waitSuppleCount
                val tempList = mutableListOf<OldManInfoWait>()
                var count = 0
                pData.forEach { bean ->
                    LogUtils.d("WaitCheckFragment", "bean:${bean}")
                    if (count < waitCount) {
                        bean?.let {
                            count++
                            tempList.add(it)
                        }
                    }
                }
                LogUtils.d("WaitCheckFragment", "size:${tempList.size}")
                adapter.submitList(tempList)
            }
        })

        /* viewModel.waitCheckLiveData.observe(this, Observer {
             adapter.submitList(it)
             getBinding().refreshWaitCheck.isRefreshing = false
         })

         viewModel.getWaitCheckNS().observe(viewLifecycleOwner, Observer {
             adapter.updateLoadingUi(it)
             LogUtils.d("status", it.name)
         })

         getBinding().refreshWaitCheck.apply {
             setColorSchemeResources(R.color.qq_blue) //设置显示颜色
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
          viewModel.waitCheckLiveData.value?.dataSource?.invalidate()
      }*/

    companion object {
        private var instance: WaitCheckFragment? = null
        fun getInstance() = instance ?: WaitCheckFragment().also {
            instance = it
        }
    }

}