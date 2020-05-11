package com.sashamprog.wallpapers

import android.app.Activity
import androidx.multidex.MultiDexApplication
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

        DaggerAppComponent.builder()
            .context(this)
            .build()
            .injectApp(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return injector
    }

}