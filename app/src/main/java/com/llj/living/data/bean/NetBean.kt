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

    data class DataBean(val isSuc: Boolean, val data: String, val expiresIn: Int)
}

class RegisterFaceBean {
    data class RegisterSuc(
        val cached: Int,
        val error_code: Int,
        val error_msg: String,
        val log_id: Long,
        val result: Result,
        val timestamp: Int
    )

    data class Result(
        val face_token: String,
        val location: Location
    )

    data class Location(
        val height: Int,
        val left: Double,
        val rotation: Int,
        val top: Double,
        val width: Int
    )

    data class RegisterErr(
        val cached: Int,
        val error_code: Int,
        val error_msg: String,
        val log_id: Long,
        val result: Any,
        val timestamp: Int
    )
}

data class CommonDataBean(val isSuc: Boolean, val data: String)