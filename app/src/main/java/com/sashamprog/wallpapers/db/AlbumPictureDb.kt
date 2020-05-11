package com.sashamprog.wallpapers.db

import com.google.gson.annotations.SerializedName

data class AlbumPictureDb(
    @SerializedName("picture_id") val pictureId: Int,
    @SerializedName("album_id") val albumId: Int
) {
    companion object {
        const val TABLE = "tbl_album_picture"
        const val COLUMN_PICTURE_ID = "column_picture_id"
        const val COLUMN_ALBUM_ID = "column_album_id"
    }
}