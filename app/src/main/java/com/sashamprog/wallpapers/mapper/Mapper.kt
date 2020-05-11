package com.sashamprog.wallpapers.mapper

import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.PixabayResponse
import com.sashamprog.wallpapers.db.AlbumDb
import com.sashamprog.wallpapers.db.AlbumPictureDb
import com.sashamprog.wallpapers.db.FavoriteDb
import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function

class FavPictureDbMapper : Function<List<FavoriteDb>, List<FavPicture>> {
    override fun apply(t: List<FavoriteDb>?): List<FavPicture> {
        return t?.map { FavPicture(it.id, it.url) } ?: emptyList()
    }
}

class FavAlbumDbMapper : Function<List<AlbumDb>, List<FavAlbum>> {
    override fun apply(t: List<AlbumDb>?): List<FavAlbum> {
        return t?.map { FavAlbum(it.id, it.name) } ?: emptyList()
    }
}

class FavAlbumDbItemMapper : Function<AlbumDb, FavAlbum> {
    override fun apply(t: AlbumDb?): FavAlbum {
        return FavAlbum(t?.id ?: -1, t?.name ?: "")
    }
}

class PixabayPictureMapper : Function<PixabayResponse?, List<Picture>> {
    override fun apply(t: PixabayResponse?): List<Picture> {
        return t?.hits?.map { Picture(it.id, it.previewUrl, it.fullUrl, false) }
            ?: emptyList()
    }
}

class PixabaySetFavoriteZipper : BiFunction<List<Picture>, List<FavPicture>, List<Picture>> {
    override fun apply(t1: List<Picture>?, t2: List<FavPicture>?): List<Picture> {
        val ids = t2?.map { it.id } ?: emptyList()
        t1?.filter { it.id in ids }
            ?.onEach { it.isFavorite = true }
        return t1 ?: emptyList()
    }

}

class AlbumPictureDbZipper : BiFunction<List<FavPicture>, List<AlbumPictureDb>, List<AlbumPicture>> {
    override fun apply(t1: List<FavPicture>?, t2: List<AlbumPictureDb>?): List<AlbumPicture> {
        return t2?.map { item ->
            val favorite = t1?.first { it.id == item.pictureId }
            AlbumPicture(item.pictureId, item.albumId, favorite?.url ?: "")
        } ?: emptyList()
    }

}