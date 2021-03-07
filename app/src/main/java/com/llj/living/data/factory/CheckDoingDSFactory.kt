package com.llj.living.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.llj.living.data.bean.CheckDoingBean
import com.llj.living.data.datasource.CheckDoingDataSource

class CheckDoingDSFactory : DataSource.Factory<Int, CheckDoingBean>() {

    val sourceLiveData = MutableLiveData<CheckDoingDataSource>()

    lateinit var checkDataSource: CheckDoingDataSource

    override fun create(): DataSource<Int, CheckDoingBean> {
        checkDataSource = CheckDoingDataSource.getInstance()
        sourceLiveData.postValue(checkDataSource)
        return checkDataSource
    }

}