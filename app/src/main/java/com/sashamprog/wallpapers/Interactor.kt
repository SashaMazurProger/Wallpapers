package com.sashamprog.wallpapers

import android.graphics.Bitmap
import com.sashamprog.wallpapers.network.PixabayResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface Interactor {
    fun isAutoChangeRunning(): Boolean
    suspend fun pictures(): Flow<List<Picture>>
    fun clear()
    fun startAutoChange(millis: Long)
    fun stopAutoChange()
    fun loadBitmap(url: String): Single<Bitmap>
    fun setWallpaper(bitmap: Bitmap): Single<Boolean>
}