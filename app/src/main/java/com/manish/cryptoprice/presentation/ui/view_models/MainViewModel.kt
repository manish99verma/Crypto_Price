package com.manish.cryptoprice.presentation.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetCoinsListUseCase

class MainViewModel(
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val get24HoursHourlyPricesListUseCase: Get24HoursHourlyPricesListUseCase
) : ViewModel() {
    public fun getCoinsList(sortBy:String) = liveData {
        getCoinsListUseCase.execute(sortBy).collect() {
            emit(it)
        }
    }

    fun get24HoursPricesList(id: String) = liveData {
        get24HoursHourlyPricesListUseCase.execute(id).collect {
            emit(it)
        }
    }

}