package com.llj.living.data.datasource

import androidx.paging.ItemKeyedDataSource
import com.llj.living.data.bean.CheckDoingBean

class CheckDoingDataSource private constructor(): ItemKeyedDataSource<Int, CheckDoingBean>() {
    private var maxId = 0
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<CheckDoingBean>
    ) {
        val list = mutableListOf<CheckDoingBean>()
        repeat(100) {
            list.add(CheckDoingBean("标题${it}", "发布时间：2020-03-08", "开始时间：2020-03-08", it))
            if (it > maxId) maxId = it
        }
        callback.onResult(list)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<CheckDoingBean>) {
        val list = mutableListOf<CheckDoingBean>()
        repeat(100) {
            maxId++
            list.add(CheckDoingBean("标题${maxId}", "发布时间：2020-03-08", "开始时间：2020-03-08", maxId))
        }
        callback.onResult(list)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<CheckDoingBean>) {}

    override fun getKey(item: CheckDoingBean): Int = item.id

    companion object{
        private var instance:CheckDoingDataSource?=null
        @Synchronized
        fun getInstance() = instance?:CheckDoingDataSource().apply {
            instance = this
        }
    }
}