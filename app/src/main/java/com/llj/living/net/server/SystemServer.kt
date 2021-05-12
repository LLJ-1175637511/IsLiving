package com.llj.living.net.server

import com.llj.living.data.bean.BaiduLLBean
import com.llj.living.data.bean.BaseBean
import com.llj.living.net.config.SysNetConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LoginServer {
    @FormUrlEncoded
    @POST("pi.php/user/alogin")
    fun login(@FieldMap map: Map<String, String>): Call<BaseBean>
}

interface LoadAPKServer {
    @Streaming
    @GET("upd/{${SysNetConfig.Path}}")
    fun loadAPK(@Path(SysNetConfig.Path) path:String): Call<ResponseBody>
}

interface SysTimeServer {
    @GET("pi.php/test/gettime")
    fun getSysTime(): Call<BaseBean>
}

interface BaiduLLServer {
    @GET("ag/coord/convert")
    fun getBaiduLL(
        @Query("from") from: Int = 0,
        @Query("to") to: Int = 4,
        @Query("x") x: Double,
        @Query("y") y: Double
    ): Call<BaiduLLBean>
}

interface VersionServer {
    @FormUrlEncoded
    @POST("pi.php/test/getversion")
    fun getVersion(@Field(SysNetConfig.CurrentVersion) currentVersion: String): Call<BaseBean>
}

interface EntInfoServer{
    @FormUrlEncoded
    @POST("pi.php/user/getnews")
    fun getEntInfo(@Field(SysNetConfig.Token) token: String,@Field(SysNetConfig.Page) page:Int): Call<BaseBean>
}

interface NewsByIdServer{
    @FormUrlEncoded
    @POST("pi.php/user/getnewsbyid")
    fun getNewsById(@Field(SysNetConfig.Token) token: String,@Field(SysNetConfig.NewId) newsId:Int): Call<BaseBean>
}

interface AdsServer{
    @FormUrlEncoded
    @POST("pi.php/user/getads")
    fun getAds(@Field(SysNetConfig.Token) token: String): Call<BaseBean>
}

/**
 * type ---> 1:已完成 2：未完成 0：全部
 */
interface EntAddonsServer{
    @FormUrlEncoded
    @POST("pi.php/addons/getentaddons")
    fun getEntAddons(
        @Field(SysNetConfig.Token) token: String,
        @Field(SysNetConfig.Page) page: Int,
        @Field(SysNetConfig.Type) type: Int
    ): Call<BaseBean>
}