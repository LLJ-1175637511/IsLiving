package com.llj.living.net.server

import com.llj.living.data.bean.BaiduLLBean
import com.llj.living.data.bean.BaseBean
import retrofit2.Call
import retrofit2.http.*

interface LoginServer {
    @FormUrlEncoded
    @POST("user/alogin")
    fun login(@FieldMap map: Map<String, String>): Call<BaseBean>
}

interface SysTimeServer {
    @GET("test/gettime")
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
    @POST("test/getversion")
    fun getVersion(@Field("curr_version") currentVersion:String): Call<BaseBean>
}