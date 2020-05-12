package com.sashamprog.wallpapers.di

import android.content.Context
import androidx.work.ListenableWorker
import com.sashamprog.wallpapers.*
import com.sashamprog.wallpapers.db.DbHelper
import com.sashamprog.wallpapers.db.DbHelperImp
import com.sashamprog.wallpapers.network.PixabayApi
import com.sashamprog.wallpapers.presenter.AlbumPresenter
import com.sashamprog.wallpapers.presenter.FavoritePresenter
import com.sashamprog.wallpapers.presenter.FullPresenter
import com.sashamprog.wallpapers.presenter.MainPresenter
import com.sashamprog.wallpapers.presenter.implement.AlbumPresenterImp
import com.sashamprog.wallpapers.presenter.implement.FavoritePresenterImp
import com.sashamprog.wallpapers.presenter.implement.FullPresenterImp
import com.sashamprog.wallpapers.presenter.implement.MainPresenterImp
import com.sashamprog.wallpapers.view.implement.AlbumActivity
import com.sashamprog.wallpapers.view.implement.FavoriteActivity
import com.sashamprog.wallpapers.view.implement.FullPictureActivity
import com.sashamprog.wallpapers.view.implement.MainActivity
import com.sashamprog.wallpapers.worker.AutoChangeWorker
import com.sashamprog.wallpapers.worker.ChildWorkerFactory
import com.sashamprog.wallpapers.worker.SampleWorkerFactory
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.*
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Scope
import javax.inject.Singleton
import kotlin.reflect.KClass

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewScope

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Singleton
@Component(
    modules = [AppModule::class,
        ViewModule::class,
        ProvideModule::class,
        SampleAssistedInjectModule::class,
        WorkerBindingModule::class]
)
interface AppComponent {

    fun injectApp(app: App)

    fun workerFactory(): SampleWorkerFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}

@Module(includes = [AssistedInject_SampleAssistedInjectModule::class])
@AssistedModule
interface SampleAssistedInjectModule

@Module(includes = [AndroidInjectionModule::class])
interface AppModule {

    @Singleton
    @Binds
    fun interactor(interactor: InteractorImp): Interactor

    @Singleton
    @Binds
    fun favoriteManager(favoriteManager: FavoriteManagerImp): FavoriteManager

    @Singleton
    @Binds
    fun dbHelper(dbHelper: DbHelperImp): DbHelper


}

@Module
class ProvideModule {

    @Singleton
    @Provides
    fun pixabayApi(): PixabayApi {
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
        return Retrofit.Builder()
            .baseUrl(PixabayApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PixabayApi::class.java)
    }
}

@Module
interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(AutoChangeWorker::class)
    fun changeWorker(factory: AutoChangeWorker.Factory): ChildWorkerFactory
}

@Module
interface ViewModule {

    @ViewScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun injectorMain(): MainActivity

    @ViewScope
    @ContributesAndroidInjector(modules = [FavoriteActivityModule::class])
    fun injectorFavorite(): FavoriteActivity

    @ViewScope
    @ContributesAndroidInjector(modules = [FullActivityModule::class])
    fun injectorFull(): FullPictureActivity

    @ViewScope
    @ContributesAndroidInjector(modules = [AlbumActivityModule::class])
    fun injectorAlbum(): AlbumActivity
}

@Module
interface MainActivityModule {
    @ViewScope
    @Binds
    fun presenter(p: MainPresenterImp): MainPresenter
}

@Module
interface FavoriteActivityModule {
    @ViewScope
    @Binds
    fun presenter(p: FavoritePresenterImp): FavoritePresenter
}

@Module
interface FullActivityModule {
    @ViewScope
    @Binds
    fun presenter(p: FullPresenterImp): FullPresenter
}

@Module
interface AlbumActivityModule {
    @ViewScope
    @Binds
    fun presenter(p: AlbumPresenterImp): AlbumPresenter
}