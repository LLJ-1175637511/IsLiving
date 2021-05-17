package com.llj.living.net.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.llj.living.net.config.BaiduNetConfig
import com.llj.living.net.network.BaiduNetWork

object BaiduRepository {

    suspend fun sendRegisterFaceRequest(
        token: String,
        map: Map<String, String>
    ) = BaiduNetWork.registerFace(token, map)

    suspend fun sendFaceRealnessRequest(
        token: String,
        data: Array<BaiduNetConfig.FaceRealnessParams>
    ) = BaiduNetWork.faceRealness(token, data)

    suspend fun sendSearchRequest(
        token: String,
        map: Map<String, String>
    ) = BaiduNetWork.searchFace(token, map)

 suspend fun sendIdcardRecognizeRequest(
        token: String,
        map: Map<String, String>
    ) = BaiduNetWork.idCardRecognize(token, map)

}