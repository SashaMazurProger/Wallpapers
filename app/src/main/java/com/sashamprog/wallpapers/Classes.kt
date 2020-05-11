package com.sashamprog.wallpapers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

//
//class FavPicture {
//
//    companion object {
//        var currentUrl = "default"
//        val favorites = mutableSetOf<FavPicture>()
//    }
//
//    var url: String = "name"
//        set(value) {
//            field = value + value
//        }
//        get() {
//            return "name"
//        }
//
//    var isCurrent = false
//        set(value) {
//            field = value
//            currentUrl = url
//        }
//        get() = url == currentUrl
//
//    fun init() {
//        val m = FavoriteManager("name", "path")
//    }
//}

interface PictureLoaderFactory {
    fun createBitmap(): Bitmap
}

class PixabayLoaderFactory(val context: Context, val url: String) : PictureLoaderFactory {
    override fun createBitmap(): Bitmap {
        return Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    }
}

class LocalLoaderFactory(val context: Context, val path: String) : PictureLoaderFactory {
    override fun createBitmap(): Bitmap {
        return BitmapFactory.decodeFile(path)
    }
}


fun <T> Single<T>.on(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.on(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Context.isOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}