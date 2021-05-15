package com.llj.living.net.network

import com.llj.living.net.RetrofitCreator
import com.llj.living.net.server.RegisterFaceServer
import com.llj.living.net.server.SearchFaceServer
import retrofit2.await

object BaiduNetWork {

    private const val contentType = "application/json" //百度云 注册人脸 固定请求头

    private val addFaceServer by lazy { RetrofitCreator.baiduCreate<RegisterFaceServer>() }

    private val searchFaceServer by lazy { RetrofitCreator.baiduCreate<SearchFaceServer>() }

    suspend fun registerFace(token: String, map: Map<String, String>) =
        addFaceServer.addFace(contentType, token, map).await()

    suspend fun searchFace(token: String, map: Map<String, String>) =
        searchFaceServer.searchFace(contentType, token, map).await()


}