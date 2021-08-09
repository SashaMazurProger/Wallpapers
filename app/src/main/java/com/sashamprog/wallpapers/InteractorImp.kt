package com.sashamprog.wallpapers

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.sashamprog.wallpapers.mapper.PixabayPictureMapper
import com.sashamprog.wallpapers.mapper.PixabaySetFavoriteZipper
import com.sashamprog.wallpapers.network.PixabayApi
import com.sashamprog.wallpapers.network.PixabayResponse
import com.sashamprog.wallpapers.worker.AutoChangeWorker
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InteractorImp @Inject constructor(
    private val favoriteManager: FavoriteManager,
    private val pixabayApi: PixabayApi,
    private val appContext: Context
) : Interactor {

    private var isAutoChange: Boolean = false

    override fun isAutoChangeRunning() = isAutoChange

    override fun loadBitmap(url: String): Single<Bitmap> {
        return Single.fromCallable {
            Glide.with(appContext)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        }
    }

    override fun stopAutoChange() {
        WorkManager.getInstance(appContext)
            .cancelAllWorkByTag("change_work")
        isAutoChange = false
    }

    override fun startAutoChange(millis: Long) {
        val request = PeriodicWorkRequestBuilder<AutoChangeWorker>(millis, TimeUnit.MILLISECONDS)
            .addTag("change_work")
            .build()

        WorkManager.getInstance(appContext)
            .enqueue(request)

        isAutoChange = true
    }

    override fun setWallpaper(bitmap: Bitmap): Single<Boolean> {
        return Single.fromCallable {
            val manager = WallpaperManager.getInstance(appContext)
            try {
                manager.setBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                return@fromCallable false
            }
            return@fromCallable true
        }
    }

    override suspend fun pictures(): Flow<List<Picture>> {
        return flow {
            emit(pixabayApi.pictures()
                .hits
                .map { Picture(it.id, it.previewUrl, it.fullUrl, false) })
        }.flowOn(Dispatchers.IO)
    }

    override fun clear() {
        favoriteManager.clear()
    }
}