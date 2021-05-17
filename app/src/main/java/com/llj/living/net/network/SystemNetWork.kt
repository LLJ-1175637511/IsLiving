package com.llj.living.net.network

import com.llj.living.net.RetrofitCreator
import com.llj.living.net.server.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await

object SystemNetWork {

    private val loginServer by lazy { RetrofitCreator.create<LoginServer>() }

    private val loadAPKServer by lazy { RetrofitCreator.create<LoadAPKServer>() }

    private val getSysTimeServer by lazy { RetrofitCreator.create<SysTimeServer>() }

    private val getBaiduLLServer by lazy { RetrofitCreator.baiduLLCreate<BaiduLLServer>() }

    private val getVersionServer by lazy { RetrofitCreator.create<VersionServer>() }

    private val getEntInfoServer by lazy { RetrofitCreator.create<EntInfoServer>() }

    private val getNewsByIdServer by lazy { RetrofitCreator.create<NewsByIdServer>() }

    private val getAdsServer by lazy { RetrofitCreator.create<AdsServer>() }

    private val getEntAddonsServer by lazy { RetrofitCreator.create<EntAddonsServer>() }

    private val getEntCheckServer by lazy { RetrofitCreator.create<EntCheckServer>() }

    private val getEntAddonsByIdServer by lazy { RetrofitCreator.create<EntAddonsByIdServer>() }

    private val getEntCheckByIdServer by lazy { RetrofitCreator.create<EntCheckByIdServer>() }

    private val getEntUploadPictureServer by lazy { RetrofitCreator.create<EntUploadPictureServer>() }

    private val getPeopleCheckSucServer by lazy { RetrofitCreator.create<PeopleCheckSucServer>() }

    private val getUploadServer by lazy { RetrofitCreator.create<UploadVideoServer>() }

    private val getTestServer by lazy { RetrofitCreator.create<TestServer>() }

    suspend fun login(map: Map<String, String>) = loginServer.login(map).await()

    suspend fun loadAPK(url: String) = loadAPKServer.loadAPK(url).await()

    suspend fun getSysTime() = getSysTimeServer.getSysTime().await()

    suspend fun getVersionTime(version: String) = getVersionServer.getVersion(version).await()

    suspend fun getEntInfo(token: String, page: Int) =
        getEntInfoServer.getEntInfo(token, page).await()

    suspend fun getBaiduLL(x: Double, y: Double) = getBaiduLLServer.getBaiduLL(x = x, y = y).await()

    suspend fun getNewsById(token: String, newsId: Int) =
        getNewsByIdServer.getNewsById(token, newsId).await()

    suspend fun getAds(token: String) = getAdsServer.getAds(token).await()

    suspend fun getEntAddons(token: String, page: Int, type: Int) =
        getEntAddonsServer.getEntAddons(token, page, type).await()

    suspend fun getEntCheck(token: String, page: Int, type: Int) =
        getEntCheckServer.getcheck(token, page, type).await()

    suspend fun getEntAddonsById(token: String, page: Int, type: Int, id: Int) =
        getEntAddonsByIdServer.getEntAddonsById(token, page, type, id).await()

    suspend fun getEntCheckById(token: String, page: Int, type: Int, id: Int) =
        getEntCheckByIdServer.getEntCheckById(token, page, type, id).await()

    suspend fun getEntUploadPicture(
        map: Map<String, RequestBody>,
        fileList: List<MultipartBody.Part>
    ) = getEntUploadPictureServer.getEntUploadPicture(map, fileList).await()

    suspend fun getPeopleCheckSuc(
        map: Map<String, RequestBody>,
        checkFile: MultipartBody.Part
    ) = getPeopleCheckSucServer.getcheckSuc(map,checkFile).await()


    suspend fun getUploadVideo(
        map: Map<String, RequestBody>,
        file: MultipartBody.Part
    ) = getUploadServer.getUploadVideo(map, file).await()


    suspend fun getTest(
        token: RequestBody,
        reputId: RequestBody,
        peopleId: RequestBody,
        faceFile: MultipartBody.Part,
        idaFile: MultipartBody.Part,
        idbFile: MultipartBody.Part
    ) = getTestServer.getTest(token, reputId, peopleId, faceFile, idaFile, idbFile).await()

}