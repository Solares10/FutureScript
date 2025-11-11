package com.example.futurescript.data.network.model

data class ApiResponse<T> (
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)
