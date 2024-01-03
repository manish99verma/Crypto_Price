package com.manish.cryptoprice.presentation.ui.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.manish.cryptoprice.data.model.ApiResponse
import com.manish.cryptoprice.domain.use_case.Get24HoursHourlyPricesListUseCase
import com.manish.cryptoprice.domain.use_case.GetCoinsListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val get24HoursHourlyPricesListUseCase: Get24HoursHourlyPricesListUseCase
) : ViewModel() {
    private val _coinsListLiveData = MutableLiveData<ApiResponse>()
    val coinsLiveData: LiveData<ApiResponse> = _coinsListLiveData

    //Using Caches
    private var lastCalledTime = 0L
    private val MIN_REQUEST_TIME = 2L * 60L * 1000L
    private lateinit var apiResponse: ApiResponse
    private var lastSorted: String = ""

    fun getCoinsList(sortBy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val diff = System.currentTimeMillis() - lastCalledTime
            if (lastSorted == sortBy && apiResponse.isSuccessful && diff < MIN_REQUEST_TIME) {
                delay(1500)

                _coinsListLiveData.postValue(apiResponse)
                Log.d("TAG", "getCoinsList: Using old data!")
                return@launch
            }

            getCoinsListUseCase.execute(sortBy).collect() {
                _coinsListLiveData.postValue(it)

                apiResponse = it
                lastSorted = sortBy
                lastCalledTime = System.currentTimeMillis()
            }
        }
    }

    fun get24HoursPricesList(id: String) = liveData {
        get24HoursHourlyPricesListUseCase.execute(id).collect {
            emit(it)
        }
    }

}