package com.manish.cryptoprice.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

    fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }
}