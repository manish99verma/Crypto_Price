package com.manish.cryptoprice

import java.io.InputStreamReader

object JsonReaderHelper {
    fun readFileResource(filename:String):String{
        val inputStream = JsonReaderHelper::class.java.getResourceAsStream(filename)
        val builder = StringBuilder()
        val reader =InputStreamReader(inputStream,"UTF-8")
        reader.readLines().forEach{
            builder.append(it)
        }
        return builder.toString()
    }
}