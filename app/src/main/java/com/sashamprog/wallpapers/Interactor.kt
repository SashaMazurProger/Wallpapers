package com.sashamprog.wallpapers

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.sashamprog.wallpapers.PixabayApi.Companion.BASE_URL
import com.sashamprog.wallpapers.mapper.PixabayPictureMapper
import com.sashamprog.wallpapers.mapper.PixabaySetFavoriteZipper
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class Interactor(context: Context) {

    private var pixabayApi: PixabayApi? = null
    private val favoriteManager = FavoriteManagerImp(context)
    var changeWallpaper: Boolean = false

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val loggingInterceptor =
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        pixabayApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PixabayApi::class.java)
    }

    fun pictures(): Observable<List<Picture>> {
        return Observable.zip(
            Observable.fromCallable {
                pixabayApi?.pictures()
                    ?.execute()
                    ?.body()
            }.map(PixabayPictureMapper()),
            favoriteManager.favorites(),
            PixabaySetFavoriteZipper()
        )
    }

    fun clear() {
        pixabayApi = null
    }
}

interface PixabayApi {

    @GET("?key=$KEY&per_page=200&orientation=vertical&q=wallpaper")
    fun pictures(): Call<PixabayResponse>

    companion object {
        const val BASE_URL = "https://pixabay.com/api/"
        const val KEY = "15693286-61af928d08c4f97ede87a7554"
    }
}

data class Picture(
    val id: Int,
    val preview: String,
    val url: String,
    var isFavorite: Boolean
)

data class PicturePixabay(
    @SerializedName("id") val id: Int,
    @SerializedName("webformatURL") val previewUrl: String,
    @SerializedName("largeImageURL") val fullUrl: String
)

data class PixabayResponse(
    @SerializedName("hits") val hits: List<PicturePixabay>
)
