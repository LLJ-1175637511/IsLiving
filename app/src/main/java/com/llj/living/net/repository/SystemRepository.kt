package com.llj.living.net.repository

import com.llj.living.net.network.SystemNetWork

object SystemRepository {

    suspend fun getSysTimeRequest() = SystemNetWork.getSysTime()

    //通过gps坐标获取百度坐标
    suspend fun getBaiduLLRequest(x:Double,y:Double) = SystemNetWork.getBaiduLL(x,y)

    suspend fun loginRequest(map: Map<String,String>) = SystemNetWork.login(map)

    suspend fun getVersionRequest(currentVersion:String) = SystemNetWork.getVersionTime(currentVersion)

}