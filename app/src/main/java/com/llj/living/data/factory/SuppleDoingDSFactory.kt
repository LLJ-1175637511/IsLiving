package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.SuppleDoingBean
import com.llj.living.data.datasource.SuppleDoingDataSource
import com.llj.living.logic.vm.SupplementViewModel

class SuppleDoingDSFactory private constructor(private val viewModel: SupplementViewModel) :
    DataSource.Factory<Int, SuppleDoingBean>() {

    private var ds: SuppleDoingDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, SuppleDoingBean> {
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