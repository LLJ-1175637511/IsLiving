package com.llj.living.net.network

import com.llj.living.application.MyApplication
import com.llj.living.data.enums.UrlType
import com.llj.living.net.RetrofitCreator
import com.llj.living.net.server.TokenServer
import retrofit2.await

object FaceAuthNetwork {

    private const val grantType = "client_credentials" //百度云 请求token默认固定参数

    private val tokenServer by lazy { RetrofitCreator.create<TokenServer>(UrlType.Token) }

    suspend fun getToken() =
        tokenServer.getToken(grantType,MyApplication.ApiKey,MyApplication.SecretKey).await()


}