package com.llj.living.net.config

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.llj.living.custom.exception.PictureLackException
import com.llj.living.data.enums.TakePhotoEnum
import com.llj.living.utils.LogUtils
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object SysNetConfig {

    const val User = "user" //用户名
    const val Pass = "pass" //用户密码
    const val Imei = "imei" //用户设备ID
    const val Lat = "lat" //用户设备ID
    const val Lng = "lng" //用户设备ID
    const val CurrentVersion = "curr_version" //用户设备ID
    const val Path = "path" //用户设备ID
    const val Token = "token" //用户token
    const val Page = "p" //新闻页数
    const val NewId = "newid" //新闻页数
    const val Type = "type" //新闻页数
    const val AddonsId = "addons_id" //补录人员详细
    const val PeopleId = "people_id" //补录人员id
    const val ReputId = "reput_id" //补录批次id
    const val Face = "face" //补录批次id
    const val Ida = "ida" //补录批次id
    const val Idb = "idb" //补录批次id

    fun buildLoginMap(
        user: String,
        pass: String,
        imei: String,
        lat: String,
        lng: String
    ) = mapOf(User to user, Pass to pass, Imei to imei, Lat to lat, Lng to lng)

    /**
     * 构造补录上传图片的普通map
     */
    fun buildUploadSuppleMap(
        token: String,
        reput_id: String,
        people_id: String
    ): Map<String, RequestBody> {
        val tmt = MediaType.parse("text/plain")
        val tokenBody = RequestBody.create(tmt, token)
        val reputIdBody = RequestBody.create(tmt, reput_id)
        val peopleIdBody = RequestBody.create(tmt, people_id)
        return mapOf(Token to tokenBody, PeopleId to reputIdBody, ReputId to peopleIdBody)
    }

    /**
     * 构造补录上传图片的文件map
     */
    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun buildUploadSuppleFileMap(
        context: Context,
        proportion: Float?
    ): List<MultipartBody.Part> {

        val faceFile = File(context.externalCacheDir, "${TakePhotoEnum.PersonFace.name}.jpg")
        val idCardAFile = File(context.externalCacheDir, "${TakePhotoEnum.IdcardFront.name}.jpg")
        val idCardBFile = File(context.externalCacheDir, "${TakePhotoEnum.IdcardBehind.name}.jpg")

        if (!faceFile.exists() || !idCardAFile.exists() || !idCardBFile.exists()) throw PictureLackException()

        val cp = Compressor(context).setQuality(25)
        proportion?.let {
            val t = (500 * proportion).toInt()
            LogUtils.d("SysNetConfig_TT","pro:${t}")
            cp.setMaxWidth(500).setMaxHeight(t)
        }
        //压缩后图片的保存路径
        val resultPath1 = cp.compressToFile(faceFile)
        //压缩后图片的保存路径
        val resultPath2 = cp.compressToFile(idCardAFile)
        //压缩后图片的保存路径
        val resultPath3 = cp.compressToFile(idCardBFile)

        LogUtils.d(
            "SysNetConfig_TT",
            "face：${resultPath1.length()} idCardA：${resultPath2.length()} idCardB：${resultPath3.length()}"
        )

        val fmt = MediaType.parse("multipart/form-data")

        val faceRequest = RequestBody.create(fmt, resultPath1)
        val idCardARequest = RequestBody.create(fmt, resultPath2)
        val idCardBRequest = RequestBody.create(fmt, resultPath3)

        val faceMultipart = MultipartBody.Part.createFormData(Face, resultPath1.name, faceRequest)
        val idCardAMultipart =
            MultipartBody.Part.createFormData(Ida, resultPath2.name, idCardARequest)
        val idCardBMultipart =
            MultipartBody.Part.createFormData(Idb, resultPath3.name, idCardBRequest)

        return listOf(faceMultipart, idCardAMultipart, idCardBMultipart)
    }

}