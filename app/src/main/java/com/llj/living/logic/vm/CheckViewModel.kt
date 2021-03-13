package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.llj.living.data.bean.MainFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.data.enums.UpdateStatusType
import com.llj.living.data.factory.CheckDoingDSFactory
import com.llj.living.data.factory.CheckFinishedDSFactory

class CheckViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseAdapterVM(application, savedStateHandle) {

    val finishedFactory = CheckFinishedDSFactory.getInstance(this)

    val finishedLiveData:LiveData<PagedList<MainFragmentBean>> = finishedFactory.toLiveData(
        20,null
    )

    val doingFactory = CheckDoingDSFactory.getInstance(this)

    val fragmentLiveData:LiveData<PagedList<MainFragmentBean>> = doingFactory.toLiveData(
        20,null
    )

    //获取 进行中 netStatusLiveData
    fun getDoingNS() = getNSLiveData(UpdateStatusType.CHECK_DOING)

    fun updateDoingNetStatus(netStatus: NetStatus){
        updateNetStatus(netStatus,UpdateStatusType.CHECK_DOING)
    }

    //获取 已完成 netStatusLiveData
    fun getFinishedNS() = getNSLiveData(UpdateStatusType.CHECK_FINISHED)

    fun updateFinishedNetStatus(netStatus: NetStatus){
        updateNetStatus(netStatus,UpdateStatusType.CHECK_FINISHED)
    }

}