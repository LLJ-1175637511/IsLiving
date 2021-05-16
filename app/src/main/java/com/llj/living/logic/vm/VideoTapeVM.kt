package com.llj.living.logic.vm

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.llj.living.custom.exception.TokenErrException
import com.llj.living.custom.ext.getSP
import com.llj.living.data.const.Const
import com.llj.living.net.config.SysNetConfig
import com.llj.living.net.repository.SystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoTapeVM(application: Application, savedStateHandle: SavedStateHandle) :
    BaseViewModel(application, savedStateHandle) {

    suspend fun uploadVideo(checkId: Int, peopleId: Int, newPath: String) = withContext(Dispatchers.Default) {
        val token = getSP(Const.SPUser).getString(Const.SPUserTokenLogin, "")
        token ?: throw TokenErrException()
        val map = SysNetConfig.buildUploadVideoMap(token, checkId.toString(), peopleId.toString())
        val videoMultipart = SysNetConfig.buildVideoPart(newPath)
        SystemRepository.getUploadVideoRequest(map,videoMultipart)
    }

}