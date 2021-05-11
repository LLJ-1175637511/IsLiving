package com.llj.living.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCreator {

    private const val faceUrl = "https://aip.baidubce.com/"
    private const val baiduLLUrl = "http://api.map.baidu.com/"
    private const val mysqlUrl = "https://p.zhuohang.tech/"


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mysqlUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val llRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baiduLLUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val baiduRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(faceUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    fun <T> baiduCreate(serviceClass: Class<T>): T =
        baiduRetrofit.create(serviceClass)

    fun <T> baiduLLCreate(serviceClass: Class<T>): T =
        llRetrofit.create(serviceClass)

    inline fun <reified T> baiduCreate(): T = baiduCreate(T::class.java)

    inline fun <reified T> baiduLLCreate(): T = baiduLLCreate(T::class.java)

    inline fun <reified T> create(): T = create(T::class.java)
}