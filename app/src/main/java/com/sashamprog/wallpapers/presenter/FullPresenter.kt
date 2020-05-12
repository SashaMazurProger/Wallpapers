package com.sashamprog.wallpapers.presenter

import com.sashamprog.wallpapers.base.MvpPresenter
import com.sashamprog.wallpapers.view.FullView

interface FullPresenter : MvpPresenter<FullView> {
    fun setUp(url: String)
    fun setBitmapAsWallpaper()
}

