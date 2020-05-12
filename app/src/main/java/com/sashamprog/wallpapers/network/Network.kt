package com.sashamprog.wallpapers.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET


interface PixabayApi {

    @GET("?key=$KEY&per_page=200&orientation=vertical&q=wallpaper")
    fun pictures(): Call<PixabayResponse>

    companion object {
        const val BASE_URL = "https://pixabay.com/api/"
        const val KEY = "15693286-61af928d08c4f97ede87a7554"
    }
}

data class PicturePixabay(
    @SerializedName("id") val id: Int,
    @SerializedName("webformatURL") val previewUrl: String,
    @SerializedName("largeImageURL") val fullUrl: String
)

data class PixabayResponse(
    @SerializedName("hits") val hits: List<PicturePixabay>
)

