package com.sashamprog.wallpapers.presenter

import com.sashamprog.wallpapers.base.MvpPresenter
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.view.AlbumView
import com.sashamprog.wallpapers.view.FullView

interface AlbumPresenter : MvpPresenter<AlbumView> {
    fun setUp()
    fun addAlbum(name: String)
    fun removeAlbum(album: FavAlbum)
}