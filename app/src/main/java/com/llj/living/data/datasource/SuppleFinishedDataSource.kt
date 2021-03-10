package com.llj.living.data.datasource

import android.annotation.SuppressLint
import androidx.paging.PageKeyedDataSource
import com.llj.living.data.bean.SuppleFinishedBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.logic.vm.SupplementViewModel
import com.llj.living.utils.LogUtils
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class SuppleFinishedDataSource(private val viewModel: SupplementViewModel) : PageKeyedDataSource<Int, SuppleFinishedBean>() {

    private val TAG = this.javaClass.simpleName

    private var retry: (() -> Any)? = null
    private var count = 0

    fun retryLoadData(){
        retry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SuppleFinishedBean>
    ) {
        retry = null
        viewModel.updateFinishedNetStatus(NetStatus.LOADING)
        LogUtils.d(TAG,"loadInitial key:${params.requestedLoadSize}")
        val firstList = mutableListOf<SuppleFinishedBean>()
        repeat(20) { num ->
            firstList.add(
                SuppleFinishedBean(
                    uName = "褚某某${num}",
                    idCard = "身份证：${(0..1000000000).random()}",
                    id = num,
                    time = "时间：${SimpleDateFormat("yyyy-mm-dd").format(Date())}"
                )
            )
        }
        callback.onResult(firstList, 0, 1)
        viewModel.updateFinishedNetStatus(NetStatus.SUCCESS)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, SuppleFinishedBean>
    ) {
        retry = null
        viewModel.updateFinishedNetStatus(NetStatus.LOADING)
        LogUtils.d(TAG,"loadAfter key:${params.key} request:${params.requestedLoadSize}")
        val firstList = mutableListOf<SuppleFinishedBean>()
        repeat(20) { num ->
            firstList.add(
                SuppleFinishedBean(
                    uName = "褚某某${num}",
                    idCard = "身份证：${(0..1000000000).random()}",
                    id = 20*params.key+num,
                    time = "时间：${SimpleDateFormat("yyyy-mm-dd").format(Date())}"
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
        callback: LoadCallback<Int, SuppleFinishedBean>
    ) {

    }
}