package com.manish.cryptoprice.presentation.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetDescriptionUseCase

class DetailsViewModel(
    private val getCoinsDescriptionUseCase: GetDescriptionUseCase,
    private val get24HoursHourlyPricesListUseCase: Get24HoursHourlyPricesListUseCase
) : ViewModel() {
    fun getCoinDetails(id:String) = liveData {
        getCoinsDescriptionUseCase.execute(id).collect() {
            emit(it)
        }
    }

    fun get24HoursPricesList(id: String) = liveData {
        get24HoursHourlyPricesListUseCase.execute(id).collect {
            emit(it)
        }
    }

}