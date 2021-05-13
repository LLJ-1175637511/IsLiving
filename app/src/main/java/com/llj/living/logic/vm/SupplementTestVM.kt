package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.llj.living.data.enums.TypeEnums
import com.llj.living.data.pagesource.AddonsByIdSource

class SupplementTestVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle){

    //获取补录完成流式数据
    fun getDoingData(id:Int) = Pager(PagingConfig(pageSize = 1)) {
        AddonsByIdSource(TypeEnums.DOING,id)
    }.flow

     //获取补录完成流式数据
    fun getFinishedData(id:Int) = Pager(PagingConfig(pageSize = 1)) {
        AddonsByIdSource(TypeEnums.FINISHED,id)
    }.flow

}