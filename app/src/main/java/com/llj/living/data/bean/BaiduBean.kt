package com.llj.living.data.bean

import com.google.gson.JsonElement

data class BaseBaiduBean(
    val cached: Int,
    val error_code: Int,
    val error_msg: String,
    val log_id: Long,
    val result: JsonElement,
    val timestamp: Int
)

data class BaiduRegisterBean(
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