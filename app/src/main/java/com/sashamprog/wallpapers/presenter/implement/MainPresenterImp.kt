package com.sashamprog.wallpapers.presenter.implement

import android.content.Context
import com.sashamprog.wallpapers.FavoriteManager
import com.sashamprog.wallpapers.FavoriteManagerImp
import com.sashamprog.wallpapers.Interactor
import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.base.BasePresenter
import com.sashamprog.wallpapers.presenter.MainPresenter
import com.sashamprog.wallpapers.view.MainView
import javax.inject.Inject

class MainPresenterImp @Inject constructor() : BasePresenter<MainView>(), MainPresenter {

    private lateinit var interactor: Interactor
    lateinit var favoriteManager: FavoriteManager

    override fun onAttach(mvpView: MainView, context: Context) {
        super.onAttach(mvpView)
        interactor = Interactor(context)
        favoriteManager = FavoriteManagerImp(context)
    }

    override fun setUp() {
        mvpView?.setAutoChangeSwitch(interactor.changeWallpaper)
        interactor.pictures()
            .lifecycle()
            .subscribe(
                { pictures ->
                    mvpView?.showPictures(pictures)
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }

    override fun onSwitchAutoChange() {
        val newValue = !interactor.changeWallpaper
        interactor.changeWallpaper = newValue
        mvpView?.setAutoChangeSwitch(newValue)
    }

    override fun removeFavorite(favoriteId: Int) {
        favoriteManager.removeFavorite(favoriteId)
            .lifecycle()
            .subscribe(
                { result -> },
                { error -> mvpView?.showMessage(error.message) }
            )
    }

    override fun addFavorite(picture: Picture) {
        favoriteManager.addFavorite(picture)
            .lifecycle()
            .subscribe(
                { result -> },
                { error -> mvpView?.showMessage(error.message) }
            )
    }
}