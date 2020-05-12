package com.sashamprog.wallpapers.presenter.implement

import com.sashamprog.wallpapers.FavoriteManager
import com.sashamprog.wallpapers.base.BasePresenter
import com.sashamprog.wallpapers.presenter.AlbumPresenter
import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.view.AlbumView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

class AlbumPresenterImp @Inject constructor(private val favoriteManager: FavoriteManager) :
    BasePresenter<AlbumView>(),
    AlbumPresenter {

    override fun setUp() {
        class Merge(val albums: List<FavAlbum>, val albumPictures: List<AlbumPicture>)

        val biFunction = BiFunction<List<FavAlbum>, List<AlbumPicture>, Merge> { t1, t2 ->
                Merge(t1 ?: emptyList(), t2 ?: emptyList()) }

        Observable.zip(
            favoriteManager.albums(),
            favoriteManager.albumPictures(),
            biFunction
        )
            .lifecycle()
            .subscribe(
                { merge ->
                    mvpView?.setAlbums(merge.albums, merge.albumPictures)
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }

    override fun removeAlbum(album: FavAlbum) {
        favoriteManager.removeAlbum(album.id)
            .lifecycle()
            .subscribe(
                { result ->
                    if (result) mvpView?.onRemoveAlbumSuccess(album.id)
                    else mvpView?.showMessage("Some problem")
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }

    override fun addAlbum(name: String) {
        favoriteManager.addAlbum(name)
            .lifecycle()
            .subscribe(
                { newAlbum ->
                    mvpView?.onAddAlbumSuccess(newAlbum)
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }
}