package com.llj.living.data.bean

import com.llj.living.data.enums.FaceType
import com.llj.living.data.enums.ImageType

data class MatchFaceData(
    val image: String,
    val image_type: ImageType,
    val face_type: FaceType = FaceType.LIVE
)

//region 主界面数据类
data class MainFragmentBean(
    val title: String,
    val endTime: String,
    val startTime: String,
    val waitDealWith:Int,
    val hadDealWith:Int,
    val id: Int
)

data class SecondFragmentBean(
    val uName: String,
    val idCard: String,
    val sex:String,
    val id: Int
)
//endregion

data class AdBean(
    val titleName: String,
    val id:Int
)