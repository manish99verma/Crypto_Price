package com.manish.cryptoprice.presentation.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetDescriptionUseCase

class DetailsViewModelFactory(
    private val getCoinsDescriptionUseCase: GetDescriptionUseCase,
    private val get24HoursHourlyPricesListUseCase: Get24HoursHourlyPricesListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(getCoinsDescriptionUseCase, get24HoursHourlyPricesListUseCase) as T
    }
}