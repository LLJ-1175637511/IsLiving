package com.llj.living.net.repository

import com.llj.living.net.network.SystemNetWork
import okhttp3.MultipartBody
import okhttp3.RequestBody

object SystemRepository {

    suspend fun getSysTimeRequest() = SystemNetWork.getSysTime()

    //通过gps坐标获取百度坐标
    suspend fun getBaiduLLRequest(x: Double, y: Double) = SystemNetWork.getBaiduLL(x, y)

    suspend fun loginRequest(map: Map<String, String>) = SystemNetWork.login(map)

    suspend fun getVersionRequest(currentVersion: String) =
        SystemNetWork.getVersionTime(currentVersion)

    suspend fun getEntInfoRequest(token: String, page: Int) = SystemNetWork.getEntInfo(token, page)

    suspend fun getNewsByIdRequest(token: String, newsId: Int) =
        SystemNetWork.getNewsById(token, newsId)

    suspend fun getAdsRequest(token: String) = SystemNetWork.getAds(token)

    suspend fun getEntAddonsRequest(token: String, page: Int, type: Int) =
        SystemNetWork.getEntAddons(token, page, type)


    suspend fun getEntCheckRequest(token: String, page: Int, type: Int) =
        SystemNetWork.getEntCheck(token, page, type)

    suspend fun getEntAddonsByIdRequest(token: String, page: Int, type: Int, id: Int) =
        SystemNetWork.getEntAddonsById(token, page, type, id)

    suspend fun getEntCheckByIdRequest(token: String, page: Int, type: Int, id: Int) =
        SystemNetWork.getEntCheckById(token, page, type, id)

    suspend fun getEntUploadPictureInfoRequest(
        map: Map<String, RequestBody>,
        fileList: List<MultipartBody.Part>
    ) = SystemNetWork.getEntUploadPicture(map, fileList)

    suspend fun getTestRequest(
        token: RequestBody,
        reputId: RequestBody,
        peopleId: RequestBody,
        faceFile: MultipartBody.Part,
        idaFile: MultipartBody.Part,
        idbFile: MultipartBody.Part
    ) = SystemNetWork.getTest(token, reputId, peopleId, faceFile, idaFile, idbFile)

    suspend fun getCheckSucRequest(
        map: Map<String, String>
    ) = SystemNetWork.getPeopleCheckSuc(map)

    suspend fun getUploadVideoRequest(
        map: Map<String, RequestBody>,
        file: MultipartBody.Part
    ) = SystemNetWork.getUploadVideo(map, file)


    suspend fun loadAPKRequest(url: String) = SystemNetWork.loadAPK(url)
}