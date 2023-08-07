package com.manish.cryptoprice.presentation.utils

import android.util.Log
import com.manish.cryptoprice.BuildConfig
import com.manish.cryptoprice.data.api.SubDirectory
import okhttp3.CacheControl
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())

        val cacheTime: Pair<Int, TimeUnit> = setCacheTime(chain.call().request().url)
        Log.d("Cache", "intercept: time: $cacheTime")

        val cacheControl = CacheControl.Builder()
            .maxAge(cacheTime.first, cacheTime.second)
            .build()

        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }

    private fun setCacheTime(httpUrl: HttpUrl): Pair<Int, TimeUnit> {
        val url = httpUrl.toString()
        Log.d("Cache", "setCacheTime: url: $url")

        if (isGetCoinList(url))
            return Pair(5, TimeUnit.MINUTES)
        else if (isGraphChart(url))
            return Pair(1, TimeUnit.HOURS)
        else if (isCoinDetails(url))
            return Pair(10, TimeUnit.DAYS)

        return Pair(2, TimeUnit.MINUTES)
    }

    private fun isGetCoinList(url: String): Boolean {
        return url.substring(
            BuildConfig.BASE_URL.length,
            BuildConfig.BASE_URL.length + SubDirectory.coinsList.length
        ) == SubDirectory.coinsList
    }

    private fun isGraphChart(url: String): Boolean {
        val indexOfMark = url.indexOf('?', BuildConfig.BASE_URL.length)

        if (indexOfMark == -1) return false

        val s1 = url.substring(BuildConfig.BASE_URL.length, indexOfMark).split('/')
        val s2 = SubDirectory.historyChart.split('/')

        return s1.size == s2.size && s1[0] == s2[0] && s1[2] == s2[2]
    }

    private fun isCoinDetails(url: String): Boolean {
        val indexOfMark = url.indexOf('?', BuildConfig.BASE_URL.length)

        if (indexOfMark == -1) return false

        val s1 = url.substring(BuildConfig.BASE_URL.length, indexOfMark).split('/')
        val s2 = SubDirectory.coinDetails.split('/')

        return s1.size == s2.size && s1[0] == s2[0]
    }


}