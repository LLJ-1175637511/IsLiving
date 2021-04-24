package com.llj.living.data.datasource

import androidx.paging.PageKeyedDataSource
import com.llj.living.application.MyApplication
import com.llj.living.custom.ext.toSimpleTime
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.utils.LogUtils

class SuppleDoingDataSource(private val viewModel: SupplementViewModel) :
    PageKeyedDataSource<Int, MainFragmentBean>() {

    private val TAG = this.javaClass.simpleName

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
        MyApplication.suppleDoingList.value?.let { callback.onResult(it, 0, 1) }
        viewModel.updateDoingNetStatus(NetStatus.SUCCESS)
    }


    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MainFragmentBean>
    ) {
        return
        retry = null
        viewModel.updateDoingNetStatus(NetStatus.LOADING)
        LogUtils.d(TAG, "loadAfter key:${params.key} request:${params.requestedLoadSize}")
        val list = mutableListOf<MainFragmentBean>()
        for (i in 1..5) {
            val waitCount = (5..11).random()
            list.add(
                MainFragmentBean(
                    title = "补录（${(10..15).random()}周${(1..3).random()}批）",
                    startTime = "2021-03-${(10..28).random()}",
                    id = i + params.key * 5,
                    endTime = (System.currentTimeMillis() + 1000 * 60 * 60 * 24).toSimpleTime(),
                    waitDealWith = waitCount,
                    hadDealWith = (0..waitCount).random()
                )
            )
        }
        if (count < 2) {
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