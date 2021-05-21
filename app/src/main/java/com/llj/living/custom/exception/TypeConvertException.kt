package com.llj.living.custom.exception

const val msg = "type is convert failed"
const val toeknErrMsg = "token not found"
const val baiduTokenErrMsg = "baidu token not found"
const val pictureErrMsg = "图片缺失"
const val faceErrMsg = "人脸信息缺失"
const val IdCardFrontErrMsg = "身份证信息错误(人脸面)"
const val IdCardFrontReversedMsg = "当前照片不是（人脸面）"
const val IdCardBackErrMsg = "身份证信息错误(国徽面)"
const val IdCardBackReversedMsg = "当前照片不是（国徽面）"
const val entIdErrMsg = "机构id有误"

class TypeConvertException : Exception(msg)

class TokenErrException : Exception(toeknErrMsg)

class BaiduTokenErrException : Exception(baiduTokenErrMsg)

class PictureLackException : Exception(pictureErrMsg)

class FaceLackException : Exception(faceErrMsg)

class IdcardFErrException : Exception(IdCardFrontErrMsg)

class IdcardFReversedException : Exception(IdCardFrontReversedMsg)

class IdcardBErrException : Exception(IdCardBackErrMsg)

class IdcardBReversedException : Exception(IdCardBackReversedMsg)

class EntIdException : Exception(entIdErrMsg)

