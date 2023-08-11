package com.manish.cryptoprice.data.model

data class ApiResponse(
    val data: Any? = null,
    val msg: String = "",
    val isSuccessful: Boolean = true
)