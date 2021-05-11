package com.llj.living.data.factory

import androidx.collection.arraySetOf
import androidx.paging.DataSource
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.datasource.WaitSuppleDataSource
import com.llj.living.logic.vm.ActSuppleViewModel

class WaitSuppleDSFactory private constructor(private val viewModel: ActSuppleViewModel) :
    DataSource.Factory<Int, SecondFragmentBean>() {

    private var ds: WaitSuppleDataSource? = null

    var arraySet = arraySetOf<Int>()

    fun retryLoadData() {
        ds?.retryLoadData()
    }

    override fun create(): DataSource<Int, SecondFragmentBean> {
        return WaitSuppleDataSource(viewModel, arraySet).also {
            ds = it
        }
    }

    fun removedId(id: Int) {
        arraySet.add(id)
    }

    companion object {
        private var instance: WaitSuppleDSFactory? = null
        fun getInstance(viewModel: ActSuppleViewModel) =
            instance ?: WaitSuppleDSFactory(viewModel).also {
                instance = it
            }

    }
}