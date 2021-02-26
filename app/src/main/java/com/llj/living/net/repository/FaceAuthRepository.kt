package com.llj.living.net.repository

import com.google.gson.reflect.TypeToken
import com.llj.living.custom.ext.isCodeSuc
import com.llj.living.custom.ext.isMsgSuc
import com.llj.living.custom.ext.stringToBean
import com.llj.living.data.bean.*
import com.llj.living.data.enums.ModifyFaceType
import com.llj.living.net.network.FaceAuthNetwork
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FaceAuthRepository {

    private const val TokenErrFlag = "error_description"
    private const val registerOrUpdateSucFlag = """"error_msg":"SUCCESS""""

    private val TAG = this.javaClass.simpleName

    suspend fun sendTokenRequest(): TokenBean.DataBean {
        val strResult = withContext(Dispatchers.IO) {//获取response
            FaceAuthNetwork.getToken().string()
        }
        return convertTokenData(strResult)
    }

    suspend fun sendModifyFaceRequest(
        token: String,
        map: Map<String, String>,
        type: ModifyFaceType
    ): CommonDataBean {
        val strResult = withContext(Dispatchers.IO) {//获取response
            when (type) {
                ModifyFaceType.Register -> FaceAuthNetwork.registerFace(token, map).string()
                ModifyFaceType.Update -> FaceAuthNetwork.updateFace(token, map).string()
            }
        }
        LogUtils.d(TAG, strResult)
        return convertRegisterOrUpdateData(strResult)
    }

    suspend fun sendDeleteFaceRequest(
        token: String,
        map: Map<String, String>
    ) = FaceAuthNetwork.deleteFace(token, map)

    suspend fun sendMatchRequest(
        token: String,
        mfbList: List<MatchFaceData>
    ): CommonDataBean = withContext(Dispatchers.Default) {
        val result = FaceAuthNetwork.matchFace(token, mfbList)//获取response
        LogUtils.d(TAG, result.toString())
        if (result.error_code.isCodeSuc() && result.error_msg.isMsgSuc()) {
            val score = result.result.score.toInt()
            if (score > 80) CommonDataBean(true, score.toString())
            else CommonDataBean(false, score.toString())
        } else CommonDataBean(false, result.error_msg)
    }

    /**
     * 响应的string 转化 RegisterFace 对象
     */
    private fun convertRegisterOrUpdateData(result: String): CommonDataBean = try {
        if (!result.contains(registerOrUpdateSucFlag)) { //请求错误 json转化错误的数据类
            val type = object : TypeToken<RegisterOrUpdateFaceBean.Failed>() {}.type
            val bean = stringToBean(result, type) as RegisterOrUpdateFaceBean.Failed
            CommonDataBean(false, bean.error_msg)
        } else { //请求错误 json转化正确请求的数据类
            val type = object : TypeToken<RegisterOrUpdateFaceBean.Success>() {}.type
            val bean = stringToBean(result, type) as RegisterOrUpdateFaceBean.Success
            CommonDataBean(true, bean.result.face_token)
        }
    } catch (e: Exception) {
        CommonDataBean(false, e.message.toString())
    }

    /**
     * 响应的string转化token对象
     */
    private fun convertTokenData(result: String): TokenBean.DataBean = try {
        if (result.contains(TokenErrFlag)) { //请求错误 json转化错误的数据类
            val type = object : TypeToken<TokenBean.TokenErr>() {}.type
            val bean = stringToBean(result, type) as TokenBean.TokenErr
            val errInfo = "error:${bean.error} description:${bean.error_description}"
            TokenBean.DataBean(false, errInfo, -1)
        } else { //请求错误 json转化正确请求的数据类
            val type = object : TypeToken<TokenBean.TokenSuc>() {}.type
            val bean = stringToBean(result, type) as TokenBean.TokenSuc
            TokenBean.DataBean(true, bean.access_token, bean.expires_in)
        }
    } catch (e: Exception) {
        TokenBean.DataBean(false, e.message.toString(), -1)
    }
}