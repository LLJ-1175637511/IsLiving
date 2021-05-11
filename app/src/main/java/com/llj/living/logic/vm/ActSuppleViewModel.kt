package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.llj.living.data.bean.SecondFragmentBean
import com.llj.living.data.enums.NetStatus
import com.llj.living.data.enums.UpdateStatusType
import com.llj.living.data.factory.HadSuppleDSFactory
import com.llj.living.data.factory.WaitSuppleDSFactory

class ActSuppleViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    BaseAdapterVM(application, savedStateHandle) {

    val waitSuppleFactory = WaitSuppleDSFactory.getInstance(this)

    val waitSuppleLiveData: LiveData<PagedList<SecondFragmentBean>> = waitSuppleFactory.toLiveData(
        20, null
    )

    val hadSuppleFactory = HadSuppleDSFactory.getInstance(this)

    val hadSuppleLiveData: LiveData<PagedList<SecondFragmentBean>> = hadSuppleFactory.toLiveData(
        20, null
    )

    //获取 进行中 netStatusLiveData
    fun getHadSuppleNS() = getNSLiveData(UpdateStatusType.WAIT_SUPPLE)

    fun updateHadSuppleNetStatus(netStatus: NetStatus) {
        updateNetStatus(netStatus, UpdateStatusType.WAIT_SUPPLE)
    }

    //获取 已完成 netStatusLiveData
    fun getWaitSuppleNS() = getNSLiveData(UpdateStatusType.HAD_SUPPLE)

    fun updateWaitSuppleNetStatus(netStatus: NetStatus) {
        updateNetStatus(netStatus, UpdateStatusType.HAD_SUPPLE)
    }

    fun refreshDataHadSource() {
        hadSuppleLiveData.value?.dataSource?.invalidate()
    }
}