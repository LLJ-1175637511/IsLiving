package com.llj.living.data.datasource

import androidx.paging.PageKeyedDataSource
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.ActSuppleViewModel
import com.llj.living.utils.LogUtils

class WaitSuppleDataSource(private val viewModel: ActSuppleViewModel) : PageKeyedDataSource<Int, SecondFragmentBean>() {

    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData(){
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SecondFragmentBean>
    ) {
        retry = null
        viewModel.updateWaitSuppleNetStatus(NetStatus.LOADING)
        LogUtils.d("CheckFinishedDataSource","loadInitial key:${params.requestedLoadSize}")
        val firstList = mutableListOf<SecondFragmentBean>()
        repeat(20) { num ->
            firstList.add(
                SecondFragmentBean(
                    uName = "褚某某${num}",
                    sex = "男",
                    id = num,
                    idCard = (10000000 + (0..88888).random()).toString()
                )
            )
        }
        callback.onResult(firstList, 0, 1)
        viewModel.updateWaitSuppleNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SecondFragmentBean>
    ) {
        retry = null
        viewModel.updateWaitSuppleNetStatus(NetStatus.LOADING)
        LogUtils.d("CheckFinishedDataSource","loadAfter key:${params.key} request:${params.requestedLoadSize}")
        val firstList = mutableListOf<SecondFragmentBean>()
        repeat(20) { num ->
            val newNum = params.key*20 + num
            firstList.add(
                SecondFragmentBean(
                    uName = "褚某某${newNum}",
                    sex = "男",
                    id = newNum,
                    idCard = (10000000 + (0..88888).random()).toString()
                )
            )
        }

        if (count<3){
            count++
            viewModel.updateWaitSuppleNetStatus(NetStatus.SUCCESS)
            callback.onResult(firstList, params.key + 1)
        }else{
            viewModel.updateWaitSuppleNetStatus(NetStatus.FAILED)
            retry = {loadAfter(params, callback)}
            count = 0
        }

    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SecondFragmentBean>
    ) {

    }
}