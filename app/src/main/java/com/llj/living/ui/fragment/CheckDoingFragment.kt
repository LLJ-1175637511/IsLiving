package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.data.database.CheckDoing
import com.llj.living.databinding.FragmentCheckDoingBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.CheckDoingAdapter

class CheckDoingFragment : NavBaseFragment<FragmentCheckDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_check_doing

//    private val viewModel by activityViewModels<CheckViewModel>()
    private val adapter by lazy { CheckDoingAdapter(dbViewModel) }
    private lateinit var pagedListLives: LiveData<PagedList<CheckDoing>>
    private val dbViewModel by activityViewModels<DatabaseVM>()

    override fun init() {
        getBinding().recyclerviewCheckDoing.adapter = adapter

        pagedListLives = LivePagedListBuilder(dbViewModel.getCheckDoingLD(),5).build()

        pagedListLives.observe(requireActivity(), Observer { data->
            data?.let {
                adapter.submitList(it)
            }
        })

        /*viewModel.fragmentLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshCheckDoing.isRefreshing = false
        })

        viewModel.getDoingNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        getBinding().refreshCheckDoing.apply {
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

    /*private fun refreshData() {
        viewModel.fragmentLiveData.value?.dataSource?.invalidate()
    }*/

}