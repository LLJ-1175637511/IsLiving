package com.llj.living.data.datasource

import androidx.paging.PageKeyedDataSource
import com.llj.living.data.bean.CheckDoingBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.CheckViewModel
import com.llj.living.utils.LogUtils

class CheckDoingDataSource(private val viewModel: CheckViewModel) :
    PageKeyedDataSource<Int, CheckDoingBean>() {

    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData() {
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CheckDoingBean>
    ) {
        retry = null
        viewModel.updateDoingNetStatus(NetStatus.LOADING)
        val list = mutableListOf<CheckDoingBean>()
        repeat(20) {
            val random = (0..1000).random()
            list.add(CheckDoingBean("标题${it}", "发布时间：2020-03-08", "开始时间：2020-03-08", random))
        }
        callback.onResult(list, 0, 1)
        viewModel.updateDoingNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CheckDoingBean>
    ) {
        retry = null
        viewModel.updateDoingNetStatus(NetStatus.LOADING)
        LogUtils.d(
            "CheckFinishedDataSource",
            "loadAfter key:${params.key} request:${params.requestedLoadSize}"
        )
        val list = mutableListOf<CheckDoingBean>()
        repeat(20) {
            val num = 20 * params.key + it
            val random = (0..1000).random()
            list.add(CheckDoingBean("标题${num}", "发布时间：2020-03-08", "开始时间：2020-03-08", random))
        }
        if (count < 3) {
            count++
            viewModel.updateDoingNetStatus(NetStatus.SUCCESS)
            callback.onResult(list, params.key + 1)
        } else {
            viewModel.updateDoingNetStatus(NetStatus.FAILED)
            retry = { loadAfter(params, callback) }
            count = 0
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CheckDoingBean>
    ) {
    }

    /*   companion object {
           private var instance: CheckFinishedDataSource? = null

           @Synchronized
           fun getInstance() = instance ?: CheckFinishedDataSource().apply {
               instance = this
           }
       }*/
}