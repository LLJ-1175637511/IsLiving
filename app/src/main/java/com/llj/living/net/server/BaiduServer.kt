package com.llj.living.net.server

import com.llj.living.data.bean.DeleteFaceBean
import com.llj.living.data.bean.MatchFaceBean
import com.llj.living.data.bean.MatchFaceData
import com.llj.living.data.bean.SearchFaceBean
import com.llj.living.net.config.BadiduNetConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TokenServer {
    @POST("oauth/2.0/token")
    fun getToken(
        @Query(BadiduNetConfig.GrantType) grantType: String,
        @Query(BadiduNetConfig.ClientId) clientId: String,
        @Query(BadiduNetConfig.ClientSecret) clientSecret: String
    ): Call<ResponseBody>
}

interface RegisterFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/faceset/user/add")
    fun addFace(
        @Header(BadiduNetConfig.ContentType) contentType:String,
        @Query(BadiduNetConfig.AccessToken) accessToken: String,
        @FieldMap map:Map<String,String>
    ): Call<ResponseBody>
}

interface UpdateFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/faceset/user/update")
    fun updateFace(
        @Header(BadiduNetConfig.ContentType) contentType:String,
        @Query(BadiduNetConfig.AccessToken) accessToken: String,
        @FieldMap map:Map<String,String>
    ): Call<ResponseBody>
}

interface DeleteFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/faceset/user/delete")
    fun deleteFace(
        @Header(BadiduNetConfig.ContentType) contentType:String,
        @Query(BadiduNetConfig.AccessToken) accessToken: String,
        @FieldMap map:Map<String,String>
    ): Call<DeleteFaceBean>
}

interface MatchFaceServer {
    @POST("rest/2.0/face/v3/match")
    fun matchFace(
        @Header(BadiduNetConfig.ContentType) contentType:String,
        @Query(BadiduNetConfig.AccessToken) accessToken: String,
        @Body faceList:List<MatchFaceData>
    ): Call<MatchFaceBean.MatchResult>
}

interface SearchFaceServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/search")
    fun searchFace(
        @Header(BadiduNetConfig.ContentType) contentType:String,
        @Query(BadiduNetConfig.AccessToken) accessToken: String,
        @FieldMap map:Map<String,String>
    ): Call<SearchFaceBean.SearchBean>
}

interface SearchFaceInZnServer {
    @FormUrlEncoded
    @POST("rest/2.0/face/v3/person/verify")
    fun searchFace(
        @Header(BadiduNetConfig.ContentType) contentType:String,
        @Query(BadiduNetConfig.AccessToken) accessToken: String,
        @FieldMap map:Map<String,String>
    ): Call<ResponseBody>
}