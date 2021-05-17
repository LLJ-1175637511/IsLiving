package com.llj.living.net.config

import com.llj.living.data.enums.ActionEnums
import com.llj.living.data.enums.IdCardEnum
import com.llj.living.data.enums.ImageType

object BaiduNetConfig {

    // region addFace params
    private const val Image = "image"
    private const val ImageType = "image_type"
    private const val GroupId = "group_id"
    private const val GroupIdList = "group_id_list"
    private const val IdCardNumber = "id_card_number"//用户身份证号
    private const val Name = "name" //用户姓名
    private const val UserId = "user_id"
    private const val UserInfo = "user_info"
    private const val FaceToken = "face_token"
    private const val ActionType = "action_type"
    private const val IdCardSide = "id_card_side"

    //endregion

    fun buildRegisterFaceMap(
        image: String,
        imageType: ImageType,
        groupId: String,
        userId: String,
        userInfo: String = ""
    ): Map<String, String> = mapOf(
        Image to image,
        ImageType to imageType.name,
        GroupId to groupId,
        UserId to userId,
        UserInfo to userInfo,
        ActionType to ActionEnums.REPLACE.name //已注册则覆盖
    )

    fun buildFaceRealnessJson(
        image: String,
        imageType: ImageType
    ): Array<FaceRealnessParams> = arrayOf(FaceRealnessParams(image,imageType.name))

    fun buildSearchFaceMap(
        image: String,
        imageType: ImageType,
        groupIdList: String
    ): Map<String, String> =
        mapOf(Image to image, ImageType to imageType.name, GroupIdList to groupIdList)

    fun buildIdCardRecognizeMap(
        image: String,
        idCardSide: IdCardEnum
    ): Map<String, String> {
        return mapOf(Image to image, IdCardSide to idCardSide.name)
    }

    data class FaceRealnessParams(
        val image: String,
        val image_type: String
    )

}