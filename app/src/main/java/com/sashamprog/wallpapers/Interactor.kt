package com.sashamprog.wallpapers

import android.graphics.Bitmap
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface Interactor {
    fun isAutoChangeRunning(): Boolean
    fun pictures(): Observable<List<Picture>>
    fun clear()
    fun startAutoChange(millis: Long)
    fun stopAutoChange()
    fun loadBitmap(url: String): Single<Bitmap>
    fun setWallpaper(bitmap: Bitmap): Single<Boolean>
}