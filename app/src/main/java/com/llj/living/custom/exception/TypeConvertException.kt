package com.llj.living.custom.exception

const val msg = "type is convert failed"
const val toeknErrMsg = "token not found"
const val pictureErrMsg = "图片缺失"

class TypeConvertException : Exception(msg)

class TokenErrException : Exception(toeknErrMsg)

class PictureLackException : Exception(pictureErrMsg)

