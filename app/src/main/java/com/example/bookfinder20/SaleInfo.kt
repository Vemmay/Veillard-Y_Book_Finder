package com.example.bookfinder20


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SaleInfo(
    @Json(name = "listPrice") val listPrice: ListPrice?,
    @Json(name = "offers") val offers: List<Offer>?,
    @Json(name = "saleability") val saleability: String
)