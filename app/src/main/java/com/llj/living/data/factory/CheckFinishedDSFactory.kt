package com.llj.living.data.factory

import androidx.paging.DataSource
import com.llj.living.data.bean.CheckFinishedBean
import com.llj.living.data.datasource.CheckFinishedDataSource

class CheckFinishedDSFactory:DataSource.Factory <Int,CheckFinishedBean>(){

    override fun create(): DataSource<Int, CheckFinishedBean> {
        return CheckFinishedDataSource()
    }

}