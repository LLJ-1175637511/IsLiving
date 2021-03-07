package com.llj.living.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.recyclerview.widget.RecyclerView
import com.llj.living.R
import com.llj.living.data.bean.CheckDoingBean
import com.llj.living.data.factory.CheckDoingDSFactory
import com.llj.living.databinding.FragmentCheckDoingBinding
import com.llj.living.ui.adapter.CheckDoingAdapter

class CheckDoingFragment : NavBaseFragment<FragmentCheckDoingBinding>() {

    override fun getLayoutId() = R.layout.fragment_check_doing

    lateinit var recyclerView: RecyclerView
    private val adapter by lazy { CheckDoingAdapter() }

    private val dataSourceFactory: CheckDoingDSFactory = CheckDoingDSFactory()

    private val dataLiveData: LiveData<PagedList<CheckDoingBean>> =
        dataSourceFactory.toLiveData(10, null)

    override fun init() {
        recyclerView = getBinding().recyclerviewCheckDoing
        recyclerView.adapter = adapter
        dataLiveData.observe(requireActivity(), Observer {
            adapter.submitList(it)
        })
        //刷新数据 invalidateDataSource()
    }

    fun invalidateDataSource() = dataSourceFactory.sourceLiveData.value?.invalidate()
}