package com.llj.living.net.network

import com.llj.living.net.RetrofitCreator
import com.llj.living.net.server.*
import retrofit2.await

object SystemNetWork {

    private val loginServer by lazy { RetrofitCreator.create<LoginServer>() }

    private val loadAPKServer by lazy { RetrofitCreator.create<LoadAPKServer>() }

    private val getSysTimeServer by lazy { RetrofitCreator.create<SysTimeServer>() }

    private val getBaiduLLServer by lazy { RetrofitCreator.baiduLLCreate<BaiduLLServer>() }

    private val getVersionServer by lazy { RetrofitCreator.create<VersionServer>() }

    private val getEntInfoServer by lazy { RetrofitCreator.create<EntInfoServer>() }

    suspend fun login(map: Map<String, String>) = loginServer.login(map).await()

    suspend fun loadAPK(url: String) = loadAPKServer.loadAPK(url).await()

    suspend fun getSysTime() = getSysTimeServer.getSysTime().await()

    suspend fun getVersionTime(version: String) = getVersionServer.getVersion(version).await()

    suspend fun getEntInfo(token: String,page:Int) = getEntInfoServer.getEntInfo(token,page).await()

    suspend fun getBaiduLL(x: Double, y: Double) = getBaiduLLServer.getBaiduLL(x = x, y = y).await()

}