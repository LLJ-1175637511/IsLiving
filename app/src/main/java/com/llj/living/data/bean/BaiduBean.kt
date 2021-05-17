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

data class SearchFaceBean(
    val face_token: String,
    val user_list: List<User>
) {
    data class User(
        val group_id: String,
        val score: Double,
        val user_id: String,
        val user_info: String
    )
}

data class IdCardBean(
    val idcard_number_type: Int,
    val image_status: String,
    val log_id: Long,
    val words_result: JsonElement,
    val words_result_num: Int
)


data class FaceRealBean(
    val face_list:JsonElement,
    val face_liveness: Double,
    val thresholds: JsonElement
)

