package com.llj.living.net.network

import com.llj.living.net.RetrofitCreator
import com.llj.living.net.server.BaiduLLServer
import com.llj.living.net.server.LoginServer
import com.llj.living.net.server.SysTimeServer
import com.llj.living.net.server.VersionServer
import retrofit2.await

object SystemNetWork {

    private val loginServer by lazy { RetrofitCreator.create<LoginServer>() }

    private val getSysTimeServer by lazy { RetrofitCreator.create<SysTimeServer>() }

    private val getBaiduLLServer by lazy { RetrofitCreator.baiduLLCreate<BaiduLLServer>() }

    private val getVersionServer by lazy { RetrofitCreator.create<VersionServer>() }

    suspend fun login(map: Map<String,String>) = loginServer.login(map).await()

    suspend fun getSysTime() = getSysTimeServer.getSysTime().await()

    suspend fun getVersionTime(version:String) = getVersionServer.getVersion(version).await()

    suspend fun getBaiduLL(x:Double,y:Double) = getBaiduLLServer.getBaiduLL(x = x,y = y).await()

}