package com.llj.living.data.bean

class TokenBean {
    data class TokenSuc(
        val access_token: String,
        val expires_in: Int,
        val refresh_token: String,
        val scope: String,
        val session_key: String,
        val session_secret: String
    )

    data class TokenErr(
        val error: String,
        val error_description: String
    )
    data class DataBean(val isSuc:Boolean,val data:String,val expiresIn:Int)
}

data class CommonDataBean(val isSuc:Boolean,val data:String)