package com.sashamprog.wallpapers.di

import android.content.Context
import com.sashamprog.wallpapers.App
import com.sashamprog.wallpapers.presenter.MainPresenter
import com.sashamprog.wallpapers.presenter.implement.MainPresenterImp
import com.sashamprog.wallpapers.view.implement.MainActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewScope

@Singleton
@Component(
    modules = [AppModule::class,
        ViewModule::class]
)
interface AppComponent {

    fun injectApp(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}

@Module(includes = [AndroidInjectionModule::class])
interface AppModule {

}

@Module
interface ViewModule {

    @ViewScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun injector(): MainActivity
}

@Module
interface MainActivityModule {
    @ViewScope
    @Binds
    fun presenter(p: MainPresenterImp): MainPresenter
}