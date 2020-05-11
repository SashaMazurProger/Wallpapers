package com.sashamprog.wallpapers.db

import com.google.gson.annotations.SerializedName

data class AlbumDb(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    companion object {
        const val TABLE = "tbl_favorite_album"
        const val COLUMN_ID = "column_id"
        const val COLUMN_NAME = "column_name"
    }
}