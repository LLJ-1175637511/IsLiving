package com.llj.living.custom.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

/***
 * v表示：
 * b表示-
 */
enum class TimeEnum {
    Y年M月D日,//2020年02月16日
    YYMMDD,//20200216
    YYbMMbDD,//2020-02-16
    YYbMMbDDHHvMMvSS //2020-02-16 10:10:10
}

@SuppressLint("SimpleDateFormat")
fun Long.convertTime(timeEnum: TimeEnum): String {
    val type = when (timeEnum) {
        TimeEnum.YYMMDD -> {
            "yyyyMMdd"
        }
        TimeEnum.Y年M月D日 -> {
            "yyyy年MM月dd日"
        }
        TimeEnum.YYbMMbDD -> {
            "yyyy-MM-dd"
        }
        TimeEnum.YYbMMbDDHHvMMvSS -> {
            "yyyy-MM-dd hh-mm-ss"
        }
    }
    return SimpleDateFormat(type).format(this)
}

