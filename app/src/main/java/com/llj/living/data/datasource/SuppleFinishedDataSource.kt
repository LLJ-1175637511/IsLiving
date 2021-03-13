package com.llj.living.data.datasource

import android.annotation.SuppressLint
import androidx.paging.PageKeyedDataSource
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.utils.LogUtils

@SuppressLint("SimpleDateFormat")
class SuppleFinishedDataSource(private val viewModel: SupplementViewModel) : PageKeyedDataSource<Int, MainFragmentBean>() {

    private val TAG = this.javaClass.simpleName

    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData(){
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MainFragmentBean>
    ) {
        retry = null
        viewModel.updateFinishedNetStatus(NetStatus.LOADING)
        LogUtils.d(TAG,"loadInitial key:${params.requestedLoadSize}")
        val firstList = mutableListOf<MainFragmentBean>()
        repeat(20) { num ->
            firstList.add(
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
        callback.onResult(firstList, 0, 1)
        viewModel.updateFinishedNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MainFragmentBean>
    ) {
        retry = null
        viewModel.updateFinishedNetStatus(NetStatus.LOADING)
        LogUtils.d(TAG,"loadAfter key:${params.key} request:${params.requestedLoadSize}")
        val firstList = mutableListOf<MainFragmentBean>()
        repeat(20) { num ->
            firstList.add(
                MainFragmentBean(
                    title = "标题${num + params.key * 20}",
                    startTime = "2021-03-08",
                    id = (0..6000).random(),
                    endTime = "2021-03-10",
                    waitDealWith = 56,
                    hadDealWith = 256
                )
            )
        }

        if (count<3){
            count++
            viewModel.updateFinishedNetStatus(NetStatus.SUCCESS)
            callback.onResult(firstList, params.key + 1)
        }else{
            viewModel.updateFinishedNetStatus(NetStatus.FAILED)
            retry = {loadAfter(params, callback)}
            count = 0
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MainFragmentBean>
    ) {

    }
}