package com.llj.living.data.datasource

import androidx.paging.PageKeyedDataSource
import com.llj.living.data.bean.CheckFinishedBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.CheckViewModel
import com.llj.living.utils.LogUtils

class CheckFinishedDataSource(private val viewModel: CheckViewModel) : PageKeyedDataSource<Int, CheckFinishedBean>() {

    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData(){
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CheckFinishedBean>
    ) {
        retry = null
        viewModel.updateFinishedNetStatus(NetStatus.LOADING)
        LogUtils.d("CheckFinishedDataSource","loadInitial key:${params.requestedLoadSize}")
        val firstList = mutableListOf<CheckFinishedBean>()
        repeat(20) { num ->
            firstList.add(
                CheckFinishedBean(
                    uName = "褚某某${num}",
                    inyardTime = "入院时间：2021-03-08",
                    age = (50..60).random(),
                    id = num,
                    num = 10000000 + (0..88888).random()
                )
            )
        }
        callback.onResult(firstList, 0, 1)
        viewModel.updateFinishedNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CheckFinishedBean>
    ) {
        retry = null
        viewModel.updateFinishedNetStatus(NetStatus.LOADING)
        LogUtils.d("CheckFinishedDataSource","loadAfter key:${params.key} request:${params.requestedLoadSize}")
        val firstList = mutableListOf<CheckFinishedBean>()
        repeat(20) { num ->
            firstList.add(
                CheckFinishedBean(
                    uName = "褚某某${num}",
                    inyardTime = "入院时间:2021-03-08",
                    age = (50..60).random(),
                    id = num,
                    num = 10000000 + (0..88888).random()
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
        callback: LoadCallback<Int, CheckFinishedBean>
    ) {

    }
}