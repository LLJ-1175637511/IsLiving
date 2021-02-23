package com.llj.living.net.server

import com.llj.living.net.config.NetConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface TokenServer {
    @POST("oauth/2.0/token")
    fun getToken(
        @Query(NetConfig.GrantType) grantType: String,
        @Query(NetConfig.ClientId) clientId: String,
        @Query(NetConfig.ClientSecret) clientSecret: String
    ): Call<ResponseBody>
}

interface FaceServer {
    @POST("oauth/2.0/token")
    fun getToken(
        @Query(NetConfig.GrantType) grantType: String,
        @Query(NetConfig.ClientId) clientId: String,
        @Query(NetConfig.ClientSecret) clientSecret: String
    ): Call<ResponseBody>
}