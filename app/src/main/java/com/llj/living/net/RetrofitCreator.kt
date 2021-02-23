package com.llj.living.net

import com.llj.living.data.enums.UrlType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCreator {

    private const val tokenUrl = "https://aip.baidubce.com/"
    private const val faceUrl = "https://aip.baidubce.com/"
    private const val mysqlUrl = "https://aip.baidubce.com/"

    fun <T> create(serviceClass: Class<T>, urlType: UrlType): T =
        buildRetrofit(urlType).create(serviceClass)

    private fun buildRetrofit(urlType: UrlType): Retrofit {
        val url = when (urlType) {
            UrlType.Face -> faceUrl
            UrlType.Token -> tokenUrl
            else -> mysqlUrl
        }
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    inline fun <reified T> create(urlType: UrlType): T = create(T::class.java, urlType)

}