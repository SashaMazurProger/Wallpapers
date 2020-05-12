package com.sashamprog.wallpapers

data class Picture(
    val id: Int,
    val preview: String,
    val url: String,
    var isFavorite: Boolean
)