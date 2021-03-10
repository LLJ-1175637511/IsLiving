package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.CheckDoingBean
import com.llj.living.data.datasource.CheckDoingDataSource
import com.llj.living.logic.vm.CheckViewModel

class CheckDoingDSFactory private constructor(private val viewModel: CheckViewModel) :
    DataSource.Factory<Int, CheckDoingBean>() {

    private var ds: CheckDoingDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, CheckDoingBean> {
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