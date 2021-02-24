package com.llj.living.net.repository

import com.google.gson.Gson
import com.llj.living.custom.exception.TypeConvertException
import com.llj.living.data.bean.TokenBean
import com.llj.living.net.network.FaceAuthNetwork
import com.llj.living.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

object FaceAuthRepository {

    private const val ErrFlag = "error_description"

    suspend fun sendTokenRequest(): TokenBean.DataBean {
        val strResult = withContext(Dispatchers.IO) {//获取response
            FaceAuthNetwork.getToken().string()
        }

        return if (strResult.contains(ErrFlag)){ //请求错误 json转化错误的数据类
            convertTokenData<TokenBean.TokenErr>{ strResult }
        }else{ //请求错误 json转化正确请求的数据类
            convertTokenData<TokenBean.TokenSuc>{ strResult }
        }
    }

    suspend fun sendRegisterFaceRequest(token:String,map: Map<String,String>): String = withContext(Dispatchers.IO){
        FaceAuthNetwork.registerFace(token, map).string()
    }

    private suspend inline fun <reified T> convertTokenData(
        context: CoroutineContext = Dispatchers.IO,
        crossinline block: suspend () -> String
    ): TokenBean.DataBean {
        val gson = Gson()
        try {
            val result = withContext(context) { //在子线程获得转化后的string
                gson.fromJson(block(), T::class.java)
            }
            if (result is TokenBean.TokenErr) { //失败
                return TokenBean.DataBean (
                    false,
                    "error:${result.error} description:${result.error_description}",
                    0
                )
            }
            if (result is TokenBean.TokenSuc) {
                return TokenBean.DataBean (
                    true,
                    result.access_token,
                    result.expires_in
                )
            } else {
                throw TypeConvertException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return  TokenBean.DataBean(
                false,
                e.message.toString(),
                -1
            )
        }
    }

}