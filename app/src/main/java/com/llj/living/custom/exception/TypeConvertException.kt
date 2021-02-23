package com.llj.living.custom.exception

class TypeConvertException(content:String = msg):Exception(content) {
    companion object{
        private const val msg = "type is not matching"
    }
}