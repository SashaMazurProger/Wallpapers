package com.sashamprog.wallpapers.db

import com.google.gson.annotations.SerializedName

data class FavoriteDb(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String
) {
    companion object {
        const val TABLE = "tbl_favorite"
        const val COLUMN_ID = "column_id"
        const val COLUMN_URL = "column_url"
    }
}