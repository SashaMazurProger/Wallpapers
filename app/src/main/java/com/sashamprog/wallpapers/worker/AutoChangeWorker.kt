package com.sashamprog.wallpapers.worker

import android.app.WallpaperManager
import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.sashamprog.wallpapers.FavoriteManager
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.Single
import kotlin.random.Random

class AutoChangeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val favoriteManager: FavoriteManager
) : RxWorker(context, workerParameters) {

    override fun createWork(): Single<Result> {
        Log.d("worker", "Change wallpaper")
        return Single.create<Result> { emitter ->
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
                            emitter.onSuccess(Result.success())
                        } catch (e: Exception) {
                            e.printStackTrace()
                            emitter.onSuccess(Result.retry())
                        }
                    },
                    { error -> emitter.onSuccess(Result.retry()) }
                )
        }
    }


//    class Factory @Inject constructor(
//        val favoriteManager: FavoriteManager
//    ) : ChildWorkerFactory {
//        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
//            return AutoChangeWorker(context, params, favoriteManager)
//        }
//    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}

