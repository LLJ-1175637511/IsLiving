package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.datasource.CheckDoingDataSource
import com.llj.living.logic.vm.CheckViewModel

class CheckDoingDSFactory private constructor(private val viewModel: CheckViewModel) :
    DataSource.Factory<Int, MainFragmentBean>() {

    private var ds: CheckDoingDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, MainFragmentBean> {
        return CheckDoingDataSource(viewModel).also {
            ds = it
        }
    }

    companion object {
        private var instance: CheckDoingDSFactory? = null
        fun getInstance(viewModel: CheckViewModel) = instance?:CheckDoingDSFactory(viewModel).apply {
            instance = this
        }
    }
}