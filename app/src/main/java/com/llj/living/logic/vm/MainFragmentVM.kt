package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.llj.living.data.datasource.EntInfoDataSource

class MainFragmentVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    //获取流式数据
    fun getData() = Pager(PagingConfig(pageSize = 1)) {
        EntInfoDataSource()
    }.flow

}