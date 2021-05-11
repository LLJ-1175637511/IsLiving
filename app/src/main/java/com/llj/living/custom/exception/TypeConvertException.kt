package com.llj.living.custom.exception

const val msg = "type is convert failed"
const val toeknErrMsg = "token not found"

class TypeConvertException: Exception(msg)

class TokenErrException : Exception(toeknErrMsg)

