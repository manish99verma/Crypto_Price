package com.manish.cryptoprice.presentation.utils

import java.text.DecimalFormat


object Utility {
    fun formatPrice(number: Double): String {//Format double to two digits after zero
        if (number == 0.0)
            return number.toString();

        val patternBuilder = StringBuilder("#.#")
        var n = number
        if (number < 0)
            n *= -1

        while (n < 1) {
            n *= 10
            patternBuilder.append("#")
        }

        val df = DecimalFormat(patternBuilder.toString())

//            Log.d("TAG", "$number -> ${df.format(number)}")
        return df.format(number)
    }
}