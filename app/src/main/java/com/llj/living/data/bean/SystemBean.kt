package com.llj.living.data.bean

import android.os.Parcelable
import com.google.gson.JsonElement
import kotlinx.android.parcel.Parcelize

/**
 * 所有借口结构基类
 */
data class BaseBean(
    val code: String,
    val msg: String,
    val result: JsonElement,
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

data class EntInfoBean(
    val news_id: Int,
    val rand: Int?,
    val title: String
)

data class NewsByIdBean(
    val content: String,
    val dt: Any,
    val news_id: Int,
    val postuser: Any,
    val rand: Any,
    val title: String
)

/**
 * 广告列表实体类（list）
 */
data class AdsBeanItem(
    val ads_id: Int,
    val img: String,
    val num: Int,
    val title: String
)

@Parcelize
data class EntAddonsBean(
    val addons_name: String,
    val id: Int,
    val people_count: Int,
    val people_reput_count: Int,
    val start_at: String,
    val end_at: String
) : Parcelable

@Parcelize
data class AddonsByEntIdBean(
    val id: Int,
    val id_number: String,
    val name: String,
    val photo: String,
    val sex: String
): Parcelable

@Parcelize
data class EntCheckBean(
    val addons_name: String,
    val id: Int,
    val people_count: Int,
    val people_check_count: Int,
    val start_at: String,
    val end_at: String
) : Parcelable
