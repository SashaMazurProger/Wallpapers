package com.sashamprog.wallpapers

import android.content.Context
import com.sashamprog.wallpapers.db.DbHelper
import com.sashamprog.wallpapers.db.DbHelperImp
import com.sashamprog.wallpapers.mapper.AlbumPictureDbZipper
import com.sashamprog.wallpapers.mapper.FavAlbumDbItemMapper
import com.sashamprog.wallpapers.mapper.FavAlbumDbMapper
import com.sashamprog.wallpapers.mapper.FavPictureDbMapper
import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavoriteManagerImp @Inject constructor(
  private val dbHelper: DbHelper
) : FavoriteManager {

    override fun addFavorite(picture: Picture): Single<Boolean> {
        return dbHelper.addFavorite(picture)
    }

    override fun removeFavorite(pictureId: Int): Single<Boolean> {
        return dbHelper.removeFavorite(pictureId)
    }

    override fun favorites(): Observable<List<FavPicture>> {
        return dbHelper.favorites()
            .map(FavPictureDbMapper())
    }

    override fun addAlbum(name: String): Single<FavAlbum> {
        return dbHelper.addAlbum(name)
            .map(FavAlbumDbItemMapper())
    }

    override fun addAlbumPicture(pictureId: Int, albumId: Int): Single<Boolean> {
        return dbHelper.addAlbumPicture(pictureId,albumId)
    }

    override fun removeAlbum(albumId: Int): Single<Boolean> {
        return dbHelper.removeAlbum(albumId)
    }

    override fun albums(): Observable<List<FavAlbum>> {
        return dbHelper.albums()
            .map(FavAlbumDbMapper())
    }

    override fun albumPictures(): Observable<List<AlbumPicture>> {
        return Observable.zip(
            dbHelper.favorites().map(FavPictureDbMapper()),
            dbHelper.albumPictures(),
            AlbumPictureDbZipper()
        )
    }

    override fun clear() {
        dbHelper.close()
    }
}