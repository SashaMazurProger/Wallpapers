package com.sashamprog.wallpapers

import android.app.Activity
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.sashamprog.wallpapers.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        val component = DaggerAppComponent.builder()
            .context(this)
            .build()

        component.injectApp(this)

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(component.workerFactory())
                .build()
        )
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return injector
    }

}