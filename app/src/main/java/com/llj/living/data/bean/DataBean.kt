package com.llj.living.data.bean

import com.llj.living.data.enums.FaceType
import com.llj.living.data.enums.ImageType

data class MatchFaceData(
    val image: String,
    val image_type: ImageType,
    val face_type: FaceType = FaceType.LIVE
)

//region Check界面数据类
data class CheckDoingBean(
    val title: String,
    val releaseTime: String,
    val startTime: String,
    val id: Int
)

data class CheckFinishedBean(
    val uName: String,
    val inyardTime: String,
    val age: Int,
    val id: Int,
    val num: Int
)
//endregion

//region Supplement界面数据类
data class SuppleDoingBean(
    val title: String,
    val name: String,
    val idCard: String,
    val id: Int
)

data class SuppleFinishedBean(
    val uName: String,
    val idCard: String,
    val id: Int,
    val time: String
)
//endregion