package com.sashamprog.wallpapers.presenter

import com.sashamprog.wallpapers.base.MvpPresenter
import com.sashamprog.wallpapers.view.FavoriteView

interface FavoritePresenter : MvpPresenter<FavoriteView> {
    fun setUp()
    fun removeFavorite(favoriteId: Int)
    fun addAlbumPicture(favoriteId: Int, albumId: Int)
}