package com.llj.living.net.server

import com.llj.living.data.bean.BaiduLLBean
import com.llj.living.data.bean.BaseBean
import com.llj.living.net.config.SysNetConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    fun loadAPK(@Path(SysNetConfig.Path) path: String): Call<ResponseBody>
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

interface EntInfoServer {
    @FormUrlEncoded
    @POST("pi.php/user/getnews")
    fun getEntInfo(
        @Field(SysNetConfig.Token) token: String,
        @Field(SysNetConfig.Page) page: Int
    ): Call<BaseBean>
}

interface NewsByIdServer {
    @FormUrlEncoded
    @POST("pi.php/user/getnewsbyid")
    fun getNewsById(
        @Field(SysNetConfig.Token) token: String,
        @Field(SysNetConfig.NewId) newsId: Int
    ): Call<BaseBean>
}

interface AdsServer {
    @FormUrlEncoded
    @POST("pi.php/user/getads")
    fun getAds(@Field(SysNetConfig.Token) token: String): Call<BaseBean>
}

/**
 * type ---> 1:已完成 2：未完成 0：全部
 */
interface EntAddonsServer {
    @FormUrlEncoded
    @POST("pi.php/addons/getentaddons")
    fun getEntAddons(
        @Field(SysNetConfig.Token) token: String,
        @Field(SysNetConfig.Page) page: Int,
        @Field(SysNetConfig.Type) type: Int
    ): Call<BaseBean>
}

/**
 * type ---> 1:已完成 2：未完成 0：全部
 */
interface EntAddonsByIdServer {
    @FormUrlEncoded
    @POST("pi.php/addons/getentaddonsbyentid")
    fun getEntAddonsById(
        @Field(SysNetConfig.Token) token: String,
        @Field(SysNetConfig.Page) page: Int,
        @Field(SysNetConfig.Type) type: Int,
        @Field(SysNetConfig.AddonsId) id: Int
    ): Call<BaseBean>
}

/**
 * type ---> 1:已完成 2：未完成 0：全部
 */
interface EntCheckByIdServer {
    @FormUrlEncoded
    @POST("pi.php/check/getentcheckbyentid")
    fun getEntCheckById(
        @Field(SysNetConfig.Token) token: String,
        @Field(SysNetConfig.Page) page: Int,
        @Field(SysNetConfig.Type) type: Int,
        @Field(SysNetConfig.CheckId) id: Int
    ): Call<BaseBean>
}

/**
 * 上传补录人员照片信息
 */
interface EntUploadPictureServer {

    @Multipart
    @POST("pi.php/addons/addonsbyid")
    fun getEntUploadPicture(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part fileList:List<MultipartBody.Part>
    ): Call<BaseBean>

}

/**
 * 上传补录人员照片信息
 */
interface TestServer {

    @Multipart
    @POST("pi.php/addons/addonsbyid")
    fun getTest(
        @Part(SysNetConfig.Token) token: RequestBody,
        @Part(SysNetConfig.ReputId) reputId: RequestBody,
        @Part(SysNetConfig.PeopleId) peopleId: RequestBody,
        @Part faceFile: MultipartBody.Part,
        @Part idaFile: MultipartBody.Part,
        @Part idbFile: MultipartBody.Part
    ): Call<BaseBean>

}

/**
 * 获取审核批次信息
 */
interface EntCheckServer {

    @FormUrlEncoded
    @POST("pi.php/check/getentcheck")
    fun getcheck(
        @Field(SysNetConfig.Token) token: String,
        @Field(SysNetConfig.Page) page: Int,
        @Field(SysNetConfig.Type) type: Int
    ): Call<BaseBean>

}

/**
 * 获取审核批次信息
 */
interface PeopleCheckSucServer {

    @FormUrlEncoded
    @POST("pi.php/check/checkbyface")
    fun getcheckSuc(
        @FieldMap map: Map<String, String>
    ): Call<BaseBean>

}

/**
 * 上传补录人员照片信息
 */
interface UploadVideoServer {

    @Multipart
    @POST("pi.php/check/checkbyvideo")
    fun getUploadVideo(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part videoFile: MultipartBody.Part
    ): Call<BaseBean>

}
