package com.llj.living.data.datasource

import androidx.paging.PageKeyedDataSource
import com.llj.living.custom.ext.toSimpleTime
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.CheckViewModel
import com.llj.living.utils.LogUtils

class CheckDoingDataSource(private val viewModel: CheckViewModel) :
    PageKeyedDataSource<Int, MainFragmentBean>() {

    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData() {
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MainFragmentBean>
    ) {
        retry = null
        viewModel.updateDoingNetStatus(NetStatus.LOADING)
        val firstList = mutableListOf<MainFragmentBean>()
        firstList.add(
            MainFragmentBean(
                title = "补录（8周3批）",
                startTime = "2021-03-${(10..15).random()}",
                id = 0,
                endTime = (System.currentTimeMillis() + 1000 * 60 * 60 * 24).toSimpleTime(),
                waitDealWith = 6,
                hadDealWith = 0
            )
        )
        firstList.add(
            MainFragmentBean(
                title = "补录（9周2批））",
                startTime = "2021-03-${(10..15).random()}",
                id = 1,
                endTime = (System.currentTimeMillis() + 1000 * 60 * 60 * 24).toSimpleTime(),
                waitDealWith = 10,
                hadDealWith = 0
            )
        )
        firstList.add(
            MainFragmentBean(
                title = "补录（9周1批）",
                startTime = "2021-03-${(10..15).random()}",
                id = 2,
                endTime = (System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 3).toSimpleTime(),
                waitDealWith = 8,
                hadDealWith = 0
            )
        )
        callback.onResult(firstList, 0, 1)
        viewModel.updateDoingNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MainFragmentBean>
    ) {
        return
        retry = null
        viewModel.updateDoingNetStatus(NetStatus.LOADING)
        LogUtils.d(
            "CheckFinishedDataSource",
            "loadAfter key:${params.key} request:${params.requestedLoadSize}"
        )
        val list = mutableListOf<MainFragmentBean>()
        repeat(20) {
            val num = 20 * params.key + it
            list.add(
                MainFragmentBean(
                    title = "标题${num}",
                    startTime = "2021-03-08",
                    id = (0..6000).random(),
                    endTime = "2021-03-10",
                    waitDealWith = 56,
                    hadDealWith = 256
                )
            )
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
        callback: LoadCallback<Int, MainFragmentBean>
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