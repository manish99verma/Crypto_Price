package com.manish.cryptoprice.presentation.ui.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.manish.cryptoprice.data.model.description.CoinDetails
import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetDescriptionUseCase

class DetailsViewModel(
    private val getCoinsDescriptionUseCase: GetDescriptionUseCase,
    private val get24HoursHourlyPricesListUseCase: Get24HoursHourlyPricesListUseCase
) : ViewModel() {
    companion object{
        //Caches
        private val caches = HashMap<String, CoinDetails>()
    }

    fun getCoinDetails(id: String) = liveData {
        if (caches.containsKey(id)) {
            emit(caches[id]!!)
            Log.d("TAG", "getCoinDetails: Using caches in viewmodel")
            return@liveData
        }

        getCoinsDescriptionUseCase.execute(id).collect() {
            emit(it)
            caches[id] = it
        }
    }

    fun get24HoursPricesList(id: String) = liveData {
        get24HoursHourlyPricesListUseCase.execute(id).collect {
            emit(it)
        }
    }

}