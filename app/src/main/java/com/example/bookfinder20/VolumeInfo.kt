package com.example.bookfinder20


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VolumeInfo(
    @Json(name = "authors") val authors: List<String>,
    @Json(name = "averageRating") val averageRating: Double?,
    @Json(name = "categories") val categories: List<String>?,
    @Json(name = "description") val description: String?,
    @Json(name = "imageLinks") val imageLinks: ImageLinks?,
    @Json(name = "language") val language: String,
    @Json(name = "maturityRating") val maturityRating: String,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "publishedDate") val publishedDate: String,
    @Json(name = "publisher") val publisher: String?,
    @Json(name = "ratingsCount") val ratingsCount: Int?,
    @Json(name = "subtitle") val subtitle: String?,
    @Json(name = "title") val title: String,

)

@JsonClass(generateAdapter = true)
data class ImageLinks(
    @Json(name = "smallThumbnail")
    val smallThumbnail: String,
    @Json(name = "thumbnail")
    val thumbnail: String
)

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "etag") val etag: String,
    @Json(name = "id") val id: String,
    @Json(name = "volumeInfo") val volumeInfo: VolumeInfo
)