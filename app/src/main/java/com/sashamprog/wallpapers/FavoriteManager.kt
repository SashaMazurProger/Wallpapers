package com.sashamprog.wallpapers

import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FavoriteManager {
    fun addFavorite(picture: Picture): Single<Boolean>
    fun addAlbum(name: String): Single<FavAlbum>
    fun removeFavorite(pictureId: Int): Single<Boolean>
    fun removeAlbum(albumId: Int): Single<Boolean>
    fun favorites(): Observable<List<FavPicture>>
    fun albums(): Observable<List<FavAlbum>>
    fun albumPictures(): Observable<List<AlbumPicture>>
    fun addAlbumPicture(pictureId: Int, albumId: Int): Single<Boolean>
}