package com.llj.living.net.network

import com.llj.living.net.RetrofitCreator
import com.llj.living.net.server.RegisterFaceServer
import retrofit2.await

object FaceAuthNetwork {

    const val grantType = "client_credentials" //百度云 请求token默认固定参数
    const val contentType = "application/json" //百度云 注册人脸 固定请求头

//    private val tokenServer by lazy { RetrofitCreator.baiduCreate<TokenServer>() }

    private val addFaceServer by lazy { RetrofitCreator.baiduCreate<RegisterFaceServer>() }

   /* private val updateFaceServer by lazy { RetrofitCreator.baiduCreate<UpdateFaceServer>() }

    private val deleteFaceServer by lazy { RetrofitCreator.baiduCreate<DeleteFaceServer>() }

    private val matchFaceServer by lazy { RetrofitCreator.baiduCreate<MatchFaceServer>() }

    private val sendFaceSearchServer by lazy { RetrofitCreator.baiduCreate<SearchFaceServer>() }

    private val sendFaceSearchInZnServer by lazy { RetrofitCreator.baiduCreate<SearchFaceInZnServer>() }
*/

   /* suspend fun getToken() =
        tokenServer.getToken(grantType, MyApplication.ApiKey, MyApplication.SecretKey).await()
*/
    suspend fun registerFace(token: String, map: Map<String, String>) =
        addFaceServer.addFace(contentType, token, map).await()

   /* suspend fun updateFace(token: String, map: Map<String, String>) =
        updateFaceServer.updateFace(contentType, token, map).await()

    suspend fun deleteFace(token: String, map: Map<String, String>) =
        deleteFaceServer.deleteFace(contentType, token, map).await()

    suspend fun matchFace(token: String, mfbList: List<MatchFaceData>) =
        matchFaceServer.matchFace(contentType, token, mfbList).await()

    suspend fun searchFace(token: String, map: Map<String, String>) =
        sendFaceSearchServer.searchFace(contentType, token, map).await()

    suspend fun searchFaceInZn(token: String, map: Map<String, String>) =
        sendFaceSearchInZnServer.searchFace(contentType, token, map).await()
*/

}