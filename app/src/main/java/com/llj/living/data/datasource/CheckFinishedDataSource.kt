package com.llj.living.data.datasource

import androidx.paging.PageKeyedDataSource
import com.llj.living.data.bean.CheckFinishedBean

class CheckFinishedDataSource : PageKeyedDataSource<Int, CheckFinishedBean>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CheckFinishedBean>
    ) {
        val firstList = mutableListOf<CheckFinishedBean>()
        repeat(100) { num ->
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
        callback.onResult(firstList, 0, null)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CheckFinishedBean>
    ) {
        val firstList = mutableListOf<CheckFinishedBean>()
        repeat(100) { num ->
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
        callback.onResult(firstList, params.key + 1)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CheckFinishedBean>
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