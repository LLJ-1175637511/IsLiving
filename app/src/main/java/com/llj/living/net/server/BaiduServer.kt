package com.llj.living.net.server

import com.llj.living.data.bean.BaseBaiduBean
import com.llj.living.net.config.TestNetConfig
import retrofit2.Call
import retrofit2.http.*

interface RegisterFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/faceset/user/add")
    fun addFace(
        @Header(TestNetConfig.ContentType) contentType: String,
        @Query(TestNetConfig.AccessToken) accessToken: String,
        @FieldMap map: Map<String, String>
    ): Call<BaseBaiduBean>
}

interface SearchFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/search")
    fun searchFace(
        @Header(TestNetConfig.ContentType) contentType: String,
        @Query(TestNetConfig.AccessToken) accessToken: String,
        @FieldMap map: Map<String, String>
    ): Call<BaseBaiduBean>
}