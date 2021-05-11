package com.llj.living.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.llj.living.R
import com.llj.living.data.database.OldManInfoHad
import com.llj.living.databinding.FragmentHadSuppleBinding
import com.llj.living.logic.vm.DatabaseVM
import com.llj.living.ui.adapter.HadSuppleAdapter

class HadSuppleFragment private constructor() : NavBaseFragment<FragmentHadSuppleBinding>() {

    override fun getLayoutId() = R.layout.fragment_had_supple

    //    private val viewModel by activityViewModels<ActSuppleViewModel>()
    private val adapter by lazy { HadSuppleAdapter(dbViewModel) }
    private lateinit var pagedListLives: LiveData<PagedList<OldManInfoHad>>
    private val dbViewModel by activityViewModels<DatabaseVM>()
    override fun init() {
        getBinding().recyclerviewHadSupple.adapter = adapter

        pagedListLives = LivePagedListBuilder(dbViewModel.getOldManInfoHadLD(), 5).build()

        pagedListLives.observe(requireActivity(), Observer { data ->
            data?.let {
                adapter.submitList(it)
            }
        })

        /*viewModel.hadSuppleLiveData.observe(this, Observer {
            adapter.submitList(it)
            getBinding().refreshHadSupple.isRefreshing = false
        })

        viewModel.getHadSuppleNS().observe(viewLifecycleOwner, Observer {
            adapter.updateLoadingUi(it)
            LogUtils.d("status", it.name)
        })

        MyApplication.suppleHadList.baseObserver(this){
            refreshData()
        }*/

        /*getBinding().refreshHadSupple.apply {
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
/*

    private fun refreshData() {
        viewModel.refreshDataHadSource()
    }
*/

    companion object {
        private var instance: HadSuppleFragment? = null
        fun getInstance() = instance ?: HadSuppleFragment().also {
            instance = it
        }
    }

}