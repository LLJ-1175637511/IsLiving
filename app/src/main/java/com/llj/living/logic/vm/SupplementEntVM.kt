package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.llj.living.data.enums.TypeEnums
import com.llj.living.data.pagesource.EntAddonsDataSource

class SupplementEntVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    //获取补录中流式数据
    fun getDoingData() = Pager(PagingConfig(pageSize = 1)) {
        EntAddonsDataSource(TypeEnums.DOING)
    }.flow

    //获取补录完成流式数据
    fun getFinishedData() = Pager(PagingConfig(pageSize = 1)) {
        EntAddonsDataSource(TypeEnums.FINISHED)
    }.flow

}