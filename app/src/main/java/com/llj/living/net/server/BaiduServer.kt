package com.llj.living.net.server

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.llj.living.data.bean.BaseBaiduBean
import com.llj.living.data.bean.IdCardBean
import com.llj.living.net.config.BaiduNetConfig
import com.llj.living.net.config.TestNetConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

//token
const val AccessToken = "access_token"

//header
const val ContentType = "Content-Type"


interface RegisterFaceServer {

    @FormUrlEncoded
    @POST("rest/2.0/face/v3/faceset/user/add")
    fun addFace(
        @Header(ContentType) contentType: String,
        @Query(AccessToken) accessToken: String,
        @FieldMap map: Map<String, String>
    ): Call<BaseBaiduBean>

}


interface FaceRealnessServer {

    @POST("rest/2.0/face/v3/faceverify")
    fun faceRealness(
        @Header(ContentType) contentType: String,
        @Query(AccessToken) accessToken: String,
        @Body data: Array<BaiduNetConfig.FaceRealnessParams>
    ): Call<BaseBaiduBean>

}

interface SearchFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/search")
    fun searchFace(
        @Header(ContentType) contentType: String,
        @Query(AccessToken) accessToken: String,
        @FieldMap map: Map<String, String>
    ): Call<BaseBaiduBean>
}

interface IdCardRecognizeServer {
    @FormUrlEncoded
    @POST("rest/2.0/ocr/v1/idcard")
    fun idCardRecognize(
        @Header(ContentType) contentType: String,
        @Query(AccessToken) accessToken: String,
        @FieldMap map: Map<String, String>
    ): Call<IdCardBean>
}