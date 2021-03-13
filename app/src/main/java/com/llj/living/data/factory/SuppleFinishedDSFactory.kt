package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.datasource.SuppleFinishedDataSource
import com.llj.living.logic.vm.SupplementViewModel

class SuppleFinishedDSFactory private constructor(private val viewModel: SupplementViewModel) :
    DataSource.Factory<Int, MainFragmentBean>() {

    private var ds: SuppleFinishedDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, MainFragmentBean> {
        return SuppleFinishedDataSource(viewModel).also {
            ds = it
        }
    }

    companion object {
        private var instance: SuppleFinishedDSFactory? = null
        fun getInstance(viewModel: SupplementViewModel) =
            instance ?: SuppleFinishedDSFactory(viewModel).also {
                instance = it
            }
    }
}