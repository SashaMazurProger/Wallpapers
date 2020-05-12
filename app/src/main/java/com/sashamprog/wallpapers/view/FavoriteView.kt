package com.sashamprog.wallpapers.view

import com.sashamprog.wallpapers.base.MvpView
import com.sashamprog.wallpapers.presenter.implement.FavoritePresenterImp
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture

interface FavoriteView:MvpView<FavoritePresenterImp> {
    fun showPictures(favorites: List<FavPicture>, albums: List<FavAlbum>)
}