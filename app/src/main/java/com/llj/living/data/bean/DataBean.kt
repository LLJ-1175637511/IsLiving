package com.llj.living.data.bean

import com.llj.living.data.enums.FaceType
import com.llj.living.data.enums.ImageType

data class MatchFaceData(
    val image:String,
    val image_type:ImageType,
    val face_type:FaceType = FaceType.LIVE
)