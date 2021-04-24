package com.llj.living.data.bean

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.llj.living.data.enums.FaceType
import com.llj.living.data.enums.ImageType
import kotlinx.android.parcel.Parcelize

data class MatchFaceData(
    val image: String,
    val image_type: ImageType,
    val face_type: FaceType = FaceType.LIVE
)

//region 主界面数据类
@Parcelize
data class MainFragmentBean(
    val title: String,
    val endTime: String,
    val startTime: String,
    var waitDealWith:Int,
    var hadDealWith:Int,
    val id: Int
) : Parcelable

@Parcelize
data class SecondFragmentBean(
    val uName: String,
    val idCard: String,
    val sex:String,
    val id: Int
) : Parcelable
//endregion

data class AdBean(
    val titleName: String,
    val id:Int,
    val addr:String
)

data class PhotoBean(
    val photoName:String,
    var uri: Uri?,
    var bitmap: Bitmap?
)