package com.llj.living.net.config

object SysNetConfig {

    const val User = "user" //用户名
    const val Pass = "pass" //用户密码
    const val Imei = "imei" //用户设备ID
    const val Lat = "lat" //用户设备ID
    const val Lng = "lng" //用户设备ID
    const val CurrentVersion = "curr_version" //用户设备ID
    const val Path = "path" //用户设备ID
    const val Token = "token" //用户token
    const val Page = "p" //新闻页数
    const val NewId = "newid" //新闻页数
    const val Type = "type" //新闻页数
    const val AddonsId = "addons_id" //补录人员详细

    fun buildLoginMap(
        user: String,
        pass: String,
        imei: String,
        lat:String,
        lng:String
    ) = mapOf(User to user, Pass to pass, Imei to imei ,Lat to lat, Lng to lng)

}