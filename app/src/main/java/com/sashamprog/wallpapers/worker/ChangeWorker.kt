package com.sashamprog.wallpapers.worker

import android.app.WallpaperManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.sashamprog.wallpapers.FavoriteManager
import com.sashamprog.wallpapers.FavoriteManagerImp
import kotlin.random.Random

class ChangeWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    val favoriteManager: FavoriteManager = FavoriteManagerImp(applicationContext)

    override fun doWork(): Result {
        Log.d("worker", "Change wallpaper")
        val manager = WallpaperManager.getInstance(applicationContext)

        favoriteManager.favorites()
            .blockingSubscribe(
                {
                    val randomPicture = it[Random.nextInt(0, it.size - 1)]
                    val bitmap = Glide.with(applicationContext)
                        .asBitmap()
                        .load(randomPicture.url)
                        .submit()
                        .get()

                    try {
                        manager.setBitmap(bitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                { error -> }
            )

        return Result.success()
    }
}