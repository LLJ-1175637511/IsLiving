package com.llj.living.utils

object StringUtils {

    fun convertMyTimeStr(str: String?): String {
        return when{
            str == null ->  "空时间"
            str.contains(" ") -> str.split(" ")[0]
            else -> "格式错误"
        }
    }

    fun convertPeopleNameStr(str: String?): String {
        return "人名：${str}"
    }

    fun convertIdNumberStr(str: String?): String {
        return "身份证：${str}"
    }
}