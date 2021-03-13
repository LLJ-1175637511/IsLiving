package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.datasource.HadCheckDataSource
import com.llj.living.logic.vm.ActCheckViewModel

class HadCheckDSFactory private constructor(private val viewModel: ActCheckViewModel) :
    DataSource.Factory<Int, SecondFragmentBean>() {

    private var ds: HadCheckDataSource? = null

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, SecondFragmentBean> {
        return HadCheckDataSource(viewModel).also {
            ds = it
        }
    }

    companion object {
        private var instance: HadCheckDSFactory? = null
        fun getInstance(viewModel: ActCheckViewModel) =
            instance ?: HadCheckDSFactory(viewModel).also {
                instance = it
            }
    }
}