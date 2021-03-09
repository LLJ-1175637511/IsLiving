package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.llj.living.data.bean.CheckDoingBean
import com.llj.living.data.bean.CheckFinishedBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.data.enums.UpdateStatusType
import com.llj.living.data.factory.CheckDoingDSFactory
import com.llj.living.data.factory.CheckFinishedDSFactory

class CheckViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseAdapterVM(application, savedStateHandle) {

    val finishedFactory = CheckFinishedDSFactory.getInstance(this)

    val finishedLiveData:LiveData<PagedList<CheckFinishedBean>> = finishedFactory.toLiveData(
        20,null
    )

    val doingFactory = CheckDoingDSFactory.getInstance(this)

    val doingLiveData:LiveData<PagedList<CheckDoingBean>> = doingFactory.toLiveData(
        20,null
    )

    fun getDoingNS() = getNSLiveData(UpdateStatusType.CHECK_DOING)

    fun updateDoingNetStatus(netStatus: NetStatus){
        updateNetStatus(netStatus,UpdateStatusType.CHECK_DOING)
    }

    fun getFinishedNS() = getNSLiveData(UpdateStatusType.CHECK_FINISHED)

    fun updateFinishedNetStatus(netStatus: NetStatus){
        updateNetStatus(netStatus,UpdateStatusType.CHECK_FINISHED)
    }

}