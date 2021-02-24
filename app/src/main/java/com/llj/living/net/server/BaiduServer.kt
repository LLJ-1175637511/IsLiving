package com.llj.living.net.server

import com.llj.living.net.config.NetConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TokenServer {
    @POST("oauth/2.0/token")
    fun getToken(
        @Query(NetConfig.GrantType) grantType: String,
        @Query(NetConfig.ClientId) clientId: String,
        @Query(NetConfig.ClientSecret) clientSecret: String
    ): Call<ResponseBody>
}

interface RegisterFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/faceset/user/add")
    fun addFace(
        @Header(NetConfig.ContentType) contentType:String,
        @Query(NetConfig.AccessToken) accessToken: String,
        @FieldMap map:Map<String,String>
    ): Call<ResponseBody>
}