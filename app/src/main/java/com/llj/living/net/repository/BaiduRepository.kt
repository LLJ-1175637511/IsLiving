package com.llj.living.net.repository

import com.llj.living.net.network.BaiduNetWork

object BaiduRepository {

    suspend fun sendRegisterFaceRequest(
        token: String,
        map: Map<String, String>
    ) = BaiduNetWork.registerFace(token, map)

    suspend fun sendSearchRequest(
        token: String,
        map: Map<String, String>
    ) = BaiduNetWork.searchFace(token, map)

}