package com.sashamprog.wallpapers.presenter.implement

import com.sashamprog.wallpapers.FavoriteManager
import com.sashamprog.wallpapers.base.BasePresenter
import com.sashamprog.wallpapers.on
import com.sashamprog.wallpapers.presenter.FavoritePresenter
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import com.sashamprog.wallpapers.view.FavoriteView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject


class FavoritePresenterImp @Inject constructor(private val favoriteManager: FavoriteManager) :
    BasePresenter<FavoriteView>(),
    FavoritePresenter {

    override fun setUp() {
        class Merge(val favorites: List<FavPicture>, val albums: List<FavAlbum>)

        val biFunction = object : BiFunction<List<FavPicture>, List<FavAlbum>, Merge> {
            override fun apply(t1: List<FavPicture>?, t2: List<FavAlbum>?): Merge {
                return Merge(t1 ?: emptyList(), t2 ?: emptyList())
            }
        }

        Observable.zip(
            favoriteManager.favorites(),
            favoriteManager.albums(),
            biFunction
        )
            .on()
            .subscribe(
                { merge ->
                    mvpView?.showPictures(merge.favorites, merge.albums)
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }

    override fun removeFavorite(favoriteId: Int) {
        favoriteManager.removeFavorite(favoriteId)
            .lifecycle()
            .subscribe(
                { result ->
                    mvpView?.showMessage("Unfavorite")
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }

    override fun addAlbumPicture(favoriteId: Int, albumId: Int) {
        favoriteManager.addAlbumPicture(favoriteId, albumId)
            .lifecycle()
            .subscribe(
                { result ->
                    if (result) {
                        mvpView?.showMessage("Success")
                    } else {
                        mvpView?.showMessage("Some problem")
                    }
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }
}
