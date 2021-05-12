package com.llj.living.net.repository

import com.llj.living.net.network.SystemNetWork

object SystemRepository {

    suspend fun getSysTimeRequest() = SystemNetWork.getSysTime()

    //通过gps坐标获取百度坐标
    suspend fun getBaiduLLRequest(x: Double, y: Double) = SystemNetWork.getBaiduLL(x, y)

    suspend fun loginRequest(map: Map<String, String>) = SystemNetWork.login(map)

    suspend fun getVersionRequest(currentVersion: String) =
        SystemNetWork.getVersionTime(currentVersion)

    suspend fun getEntInfoRequest(token: String,page:Int) = SystemNetWork.getEntInfo(token,page)

    suspend fun getNewsByIdRequest(token: String,newsId:Int) = SystemNetWork.getNewsById(token,newsId)

    suspend fun getAdsRequest(token: String) = SystemNetWork.getAds(token)

    suspend fun loadAPKRequest(url: String) = SystemNetWork.loadAPK(url)
}