package com.sashamprog.wallpapers.presenter.implement

import com.sashamprog.wallpapers.FavoriteManager
import com.sashamprog.wallpapers.Interactor
import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.base.BasePresenter
import com.sashamprog.wallpapers.presenter.MainPresenter
import com.sashamprog.wallpapers.view.MainView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainPresenterImp @Inject constructor(
    private val favoriteManager: FavoriteManager,
    private val interactor: Interactor
) : BasePresenter<MainView>(), MainPresenter {

    override fun setUp() {
        mvpView?.setAutoChangeSwitch(interactor.isAutoChangeRunning())
        GlobalScope.launch(Dispatchers.Main) {
            interactor.pictures()
                .catch { mvpView?.showMessage(it.message) }
                .collect { mvpView?.showPictures(it) }
        }
    }

    override fun onSwitchAutoChange(millisPeriod: Long) {
        val newValue = !interactor.isAutoChangeRunning()
        if (newValue) interactor.startAutoChange(millisPeriod)
        else interactor.stopAutoChange()

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

    override fun onDetach() {
        super.onDetach()
        interactor.clear()
    }
}

