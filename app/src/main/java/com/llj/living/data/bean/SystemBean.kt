package com.llj.living.data.bean

import android.os.Parcelable
import com.google.gson.JsonObject
import kotlinx.android.parcel.Parcelize

data class BaseBean(
    val code: String,
    val msg: String,
    val result: JsonObject,
    val token: String
)

/**
 * 登录数据
 */
@Parcelize
data class LoginBean(
    val avatar: String,
    val ent_address: String,
    val ent_bednum: Int,
    val ent_id: Int,
    val ent_lat: Double,
    val ent_leave: Int,
    val ent_lng: Double,
    val ent_name: String,
    val ent_people: Int,
    val nickname: String,
    val token: String,
    val user_id: Int
) : Parcelable

/**
 * 获取服务器时间（校验）
 */
data class SysTimeBean(
    val time: Long
)

data class VersionBean(
    val content: String,
    val createtime: Int,
    val downloadurl: String,
    val enforce: Int,
    val id: Int,
    val newversion: String,
    val oldversion: String,
    val packagesize: String,
    val status: String,
    val updatetime: Int,
    val weigh: Int
)

/**
 * 获取GPS转化后的百度坐标
 */
data class BaiduLLBean(
    val error: Int,
    val x: String,
    val y: String
)

