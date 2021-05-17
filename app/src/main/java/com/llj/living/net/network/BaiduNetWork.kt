package com.llj.living.net.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.llj.living.net.RetrofitCreator
import com.llj.living.net.config.BaiduNetConfig
import com.llj.living.net.server.FaceRealnessServer
import com.llj.living.net.server.IdCardRecognizeServer
import com.llj.living.net.server.RegisterFaceServer
import com.llj.living.net.server.SearchFaceServer
import retrofit2.await

object BaiduNetWork {

    private const val contentTypeJson = "application/json" //百度云 注册人脸 固定请求头
    private const val contentTypeForm = "application/x-www-form-urlencoded" //百度云 注册人脸 固定请求头

    private val addFaceServer by lazy { RetrofitCreator.baiduCreate<RegisterFaceServer>() }

    private val faceRealnessServer by lazy { RetrofitCreator.baiduCreate<FaceRealnessServer>() }

    private val searchFaceServer by lazy { RetrofitCreator.baiduCreate<SearchFaceServer>() }

    private val idCardRecognizeServer by lazy { RetrofitCreator.baiduCreate<IdCardRecognizeServer>() }

    suspend fun registerFace(token: String, map: Map<String, String>) =
        addFaceServer.addFace(contentTypeJson, token, map).await()

    suspend fun faceRealness(token: String, data: Array<BaiduNetConfig.FaceRealnessParams>) =
        faceRealnessServer.faceRealness(contentTypeJson, token, data).await()

    suspend fun searchFace(token: String, map: Map<String, String>) =
        searchFaceServer.searchFace(contentTypeJson, token, map).await()

    suspend fun idCardRecognize(token: String, map: Map<String, String>) =
        idCardRecognizeServer.idCardRecognize(contentTypeForm, token, map).await()


}