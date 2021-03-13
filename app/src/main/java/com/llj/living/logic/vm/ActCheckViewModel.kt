package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.data.enums.UpdateStatusType
import com.llj.living.data.factory.HadCheckDSFactory
import com.llj.living.data.factory.WaitCheckDSFactory

class ActCheckViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseAdapterVM(application, savedStateHandle) {

    val waitCheckFactory = WaitCheckDSFactory.getInstance(this)

    val waitCheckLiveData:LiveData<PagedList<SecondFragmentBean>> = waitCheckFactory.toLiveData(
        20,null
    )

    val hadCheckFactory = HadCheckDSFactory.getInstance(this)

    val hadCheckLiveData:LiveData<PagedList<SecondFragmentBean>> = hadCheckFactory.toLiveData(
        20,null
    )

    //获取 进行中 netStatusLiveData
    fun getHadCheckNS() = getNSLiveData(UpdateStatusType.WAIT_CHECK)

    fun updateHadCheckNetStatus(netStatus: NetStatus){
        updateNetStatus(netStatus,UpdateStatusType.WAIT_CHECK)
    }

    //获取 已完成 netStatusLiveData
    fun getWaitCheckNS() = getNSLiveData(UpdateStatusType.HAD_CHECK)

    fun updateWaitCheckNetStatus(netStatus: NetStatus){
        updateNetStatus(netStatus,UpdateStatusType.HAD_CHECK)
    }

}