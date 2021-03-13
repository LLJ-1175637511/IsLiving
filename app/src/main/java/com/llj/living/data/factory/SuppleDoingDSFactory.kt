package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.datasource.SuppleDoingDataSource
import com.llj.living.logic.vm.SupplementViewModel

class SuppleDoingDSFactory private constructor(private val viewModel: SupplementViewModel) :
    DataSource.Factory<Int, MainFragmentBean>() {

    private var ds: SuppleDoingDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, MainFragmentBean> {
        return SuppleDoingDataSource(viewModel).also {
            ds = it
        }
    }

    companion object {
        private var instance: SuppleDoingDSFactory? = null
        fun getInstance(viewModel: SupplementViewModel) = instance?:SuppleDoingDSFactory(viewModel).apply {
            instance = this
        }
    }
}