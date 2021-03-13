package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.datasource.HadSuppleDataSource
import com.llj.living.logic.vm.ActSuppleViewModel

class HadSuppleDSFactory private constructor(private val viewModel: ActSuppleViewModel) :
    DataSource.Factory<Int, SecondFragmentBean>() {

    private var ds: HadSuppleDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, SecondFragmentBean> {
        return HadSuppleDataSource(viewModel).also {
            ds = it
        }
    }

    companion object {
        private var instance: HadSuppleDSFactory? = null
        fun getInstance(viewModel: ActSuppleViewModel) =
            instance ?: HadSuppleDSFactory(viewModel).also {
                instance = it
            }
    }
}