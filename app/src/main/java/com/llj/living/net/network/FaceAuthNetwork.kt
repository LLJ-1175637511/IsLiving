package com.llj.living.net.network

import com.llj.living.application.MyApplication
import com.llj.living.data.bean.MatchFaceData
import com.llj.living.data.enums.UrlType
import com.llj.living.net.RetrofitCreator
import com.llj.living.net.server.*
import retrofit2.await

object FaceAuthNetwork {

    private const val grantType = "client_credentials" //百度云 请求token默认固定参数
    private const val contentType = "application/json" //百度云 注册人脸 固定请求头

    private val tokenServer by lazy { RetrofitCreator.create<TokenServer>(UrlType.Token) }

    private val addFaceServer by lazy { RetrofitCreator.create<RegisterFaceServer>(UrlType.Face) }

    private val updateFaceServer by lazy { RetrofitCreator.create<UpdateFaceServer>(UrlType.Face) }

    private val deleteFaceServer by lazy { RetrofitCreator.create<DeleteFaceServer>(UrlType.Face) }

    private val matchFaceServer by lazy { RetrofitCreator.create<MatchFaceServer>(UrlType.Face) }

    suspend fun getToken() =
        tokenServer.getToken(grantType,MyApplication.ApiKey,MyApplication.SecretKey).await()

    suspend fun registerFace(token:String,map: Map<String,String>) = addFaceServer.addFace(contentType,token,map).await()

    suspend fun updateFace(token:String,map: Map<String,String>) = updateFaceServer.updateFace(contentType,token,map).await()

    suspend fun deleteFace(token:String,map: Map<String,String>) = deleteFaceServer.deleteFace(contentType,token,map).await()

    suspend fun matchFace(token:String,mfbList: List<MatchFaceData>) = matchFaceServer.matchFace(contentType,token,mfbList).await()

}