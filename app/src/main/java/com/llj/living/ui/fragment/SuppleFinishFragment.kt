package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.data.database.SuppleFinished
import com.llj.living.databinding.FragmentSuppleFinishedBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.SuppleFinishedAdapter

class SuppleFinishFragment private constructor():NavBaseFragment<FragmentSuppleFinishedBinding>() {

    override fun getLayoutId() = R.layout.fragment_supple_finished

//    private val viewModel by activityViewModels<SupplementViewModel>()
//    private val adapter by lazy { SuppleFinishedAdapter(viewModel) }
    private val adapter by lazy { SuppleFinishedAdapter() }
    private val dbViewModel by activityViewModels<DatabaseVM>()
    private lateinit var pagedListLives: LiveData<PagedList<SuppleFinished>>
    override fun init() {
        getBinding().recyclerviewSuppleFinished.adapter = adapter

      /*  viewModel.finishedLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshSuppleFinished.isRefreshing = false
        })

        viewModel.getFinishedNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })*/

        pagedListLives = LivePagedListBuilder(dbViewModel.getSuppleFinishedListLD(),5).build()

        pagedListLives.observe(requireActivity(), Observer { data->
            data?.let {
                adapter.submitList(it)
            }
        })

        /*getBinding().refreshSuppleFinished.apply {
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

   /* private fun refreshData() {
        viewModel.finishedLiveData.value?.dataSource?.invalidate()
    }*/

    companion object{
        private var instance:SuppleFinishFragment ?= null
        fun getInstance() = instance?:SuppleFinishFragment()
    }
}