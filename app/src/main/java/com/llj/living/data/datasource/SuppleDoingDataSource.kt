package com.llj.living.data.datasource

import androidx.paging.PageKeyedDataSource
import com.llj.living.data.bean.SuppleDoingBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.utils.LogUtils

class SuppleDoingDataSource(private val viewModel: SupplementViewModel) :
    PageKeyedDataSource<Int, SuppleDoingBean>() {

    private val TAG = this.javaClass.simpleName

    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData() {
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SuppleDoingBean>
    ) {
        retry = null
        viewModel.updateDoingNetStatus(NetStatus.LOADING)
        val list = mutableListOf<SuppleDoingBean>()
        repeat(20) {
            list.add(SuppleDoingBean("标题${it}", "姓名：钱某某", "身份证：${(0..1000000000).random()}",it))
        }
        callback.onResult(list, 0, 1)
        viewModel.updateDoingNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SuppleDoingBean>
    ) {
        retry = null
        viewModel.updateDoingNetStatus(NetStatus.LOADING)
        LogUtils.d(TAG, "loadAfter key:${params.key} request:${params.requestedLoadSize}")
        val list = mutableListOf<SuppleDoingBean>()
        repeat(20) {
            val num = 20 * params.key + it
            list.add(SuppleDoingBean("标题${num}", "姓名：钱某某", "身份证：${(0..1000000000).random()}",num))
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
        callback: LoadCallback<Int, SuppleDoingBean>
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