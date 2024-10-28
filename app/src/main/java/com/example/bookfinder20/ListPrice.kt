package com.example.bookfinder20


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListPrice(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "currencyCode")
    val currencyCode: String
)