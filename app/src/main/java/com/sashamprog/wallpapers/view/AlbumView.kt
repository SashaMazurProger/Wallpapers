package com.sashamprog.wallpapers.view

import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.base.MvpView
import com.sashamprog.wallpapers.presenter.AlbumPresenter
import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum

interface AlbumView : MvpView<AlbumPresenter> {
    fun onAddAlbumSuccess(newAlbum: FavAlbum?)
    fun setAlbums(albums: List<FavAlbum>, albumPictures: List<AlbumPicture>)
    fun onRemoveAlbumSuccess(albumId: Int)
}