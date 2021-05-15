package com.llj.living.net.config


import com.llj.living.data.enums.ImageType

object TestNetConfig {
    //token
    const val AccessToken = "access_token"

    //header
    const val ContentType = "Content-Type"

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
    //endregion

    fun buildRegisterOrUpdateFaceMap(
        image: String,
        imageType: ImageType,
        groupId: String,
        userId: String,
        userInfo: String
    ): Map<String, String> = mapOf(
        Image to image,
        ImageType to imageType.name,
        GroupId to groupId,
        UserId to userId,
        UserInfo to userInfo
    )


    fun buildDeleteFaceMap(
        groupId: String,
        userId: String,
        faceToken: String
    ): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map[GroupId] = groupId
        map[UserId] = userId
        map[FaceToken] = faceToken
        return map
    }

    fun buildSearchFaceMap(
        image: String,
        imageType: ImageType,
        groupIdList: String
    ): Map<String, String> =
        mapOf(Image to image, ImageType to imageType.name, GroupIdList to groupIdList)

    fun buildSearchFaceInZnMap(
        image: String,
        imageType: ImageType,
        name: String,
        idCardNumber: String
    ): Map<String, String> =
        mapOf(
            Image to image,
            ImageType to imageType.name,
            Name to name,
            IdCardNumber to idCardNumber
        )

}