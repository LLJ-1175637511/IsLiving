package com.llj.living.data.enums


/**
 * 服务器基地址（baseUrl）
 * Face:百度云
 * Mysql：本系统
 */
enum class UrlType{
    Face,Token,Mysql
}

/**
 * 请求图片类型
 * BASE64：base64
 * FACE_TOKEN：人脸库唯一ID
 * URL：图片链接（不推荐 网络原因 可能会导致百度云下载图片失败）
 */
enum class ImageType{
    BASE64,FACE_TOKEN,URL
}

enum class ActionType{
    APPEND,REPLACE,UPDATE
}

enum class ModifyFaceType{
    Register,Update
}

/**
 * LIVE：生活照 默认
 * IDCARD：身份证芯片照
 * WATERMARK：带水印证件照
 * CERT：证件照片
 * INFRARED：红外照片
 */
enum class FaceType{
    LIVE,IDCARD,WATERMARK,CERT,INFRARED
}
