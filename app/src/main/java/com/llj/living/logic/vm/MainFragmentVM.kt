package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.llj.living.custom.ext.quickTokenRequest
import com.llj.living.custom.ext.tryExceptionLaunch
import com.llj.living.data.bean.AdsBeanItem
import com.llj.living.data.pagesource.EntInfoDataSource
import com.llj.living.net.repository.SystemRepository

class MainFragmentVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    private val _adsLiveData = MutableLiveData<List<AdsBeanItem>>()
    val adsLiveData: LiveData<List<AdsBeanItem>> = _adsLiveData

    //获取流式数据
    fun getData() = Pager(PagingConfig(pageSize = 1)) {
        EntInfoDataSource()
    }.flow

    /**
     * 获取广告数据
     */
    fun loadAdsData() = tryExceptionLaunch {
        val adsBean = quickTokenRequest<List<AdsBeanItem>> { token ->
            SystemRepository.getAdsRequest(token)
        }?: return@tryExceptionLaunch
        _adsLiveData.postValue(adsBean)
    }

}