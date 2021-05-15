package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.llj.living.data.enums.TypeEnums
import com.llj.living.data.pagesource.CheckByIdSource

class CheckPeopleVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    //获取补录中流式数据
    fun getDoingData(checkId:Int) = Pager(PagingConfig(pageSize = 1)) {
        CheckByIdSource(TypeEnums.DOING,checkId)
    }.flow

    //获取补录完成流式数据
    fun getFinishedData(checkId:Int) = Pager(PagingConfig(pageSize = 1)) {
        CheckByIdSource(TypeEnums.FINISHED,checkId)
    }.flow

}