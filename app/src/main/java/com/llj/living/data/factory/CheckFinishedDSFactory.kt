package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.datasource.CheckFinishedDataSource
import com.llj.living.logic.vm.CheckViewModel

class CheckFinishedDSFactory private constructor(private val viewModel: CheckViewModel) :
    DataSource.Factory<Int, MainFragmentBean>() {

    private var ds: CheckFinishedDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, MainFragmentBean> {
        return CheckFinishedDataSource(viewModel).also {
            ds = it
        }
    }

    companion object {
        private var instance: CheckFinishedDSFactory? = null
        fun getInstance(viewModel: CheckViewModel) =
            instance ?: CheckFinishedDSFactory(viewModel).also {
                instance = it
            }
    }
}