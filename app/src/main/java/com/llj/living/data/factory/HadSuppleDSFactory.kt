package com.llj.living.data.factory

import androidx.collection.arraySetOf
import androidx.paging.DataSource
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.datasource.HadSuppleDataSource
import com.llj.living.logic.vm.ActSuppleViewModel

class HadSuppleDSFactory private constructor(private val viewModel: ActSuppleViewModel) :
    DataSource.Factory<Int, SecondFragmentBean>() {

    private var ds: HadSuppleDataSource? = null

    private var arraySet = arraySetOf<Int>()

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, SecondFragmentBean> {
        return HadSuppleDataSource(viewModel, arraySet).also {
            ds = it
        }
    }

    fun addItem(id: Int) {
        arraySet.add(id)
    }

    companion object {
        private var instance: HadSuppleDSFactory? = null
        fun getInstance(viewModel: ActSuppleViewModel) =
            instance ?: HadSuppleDSFactory(viewModel).also {
                instance = it
            }
    }
}