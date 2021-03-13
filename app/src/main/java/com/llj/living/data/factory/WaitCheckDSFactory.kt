package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.datasource.WaitCheckDataSource
import com.llj.living.logic.vm.ActCheckViewModel

class WaitCheckDSFactory private constructor(private val viewModel: ActCheckViewModel) :
    DataSource.Factory<Int, SecondFragmentBean>() {

    private var ds: WaitCheckDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, SecondFragmentBean> {
        return WaitCheckDataSource(viewModel).also {
            ds = it
        }
    }

    companion object {
        private var instance: WaitCheckDSFactory? = null
        fun getInstance(viewModel: ActCheckViewModel) =
            instance ?: WaitCheckDSFactory(viewModel).also {
                instance = it
            }
    }
}