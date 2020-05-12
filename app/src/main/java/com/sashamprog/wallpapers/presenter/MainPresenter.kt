package com.sashamprog.wallpapers.presenter

import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.base.MvpPresenter
import com.sashamprog.wallpapers.view.MainView

interface MainPresenter : MvpPresenter<MainView> {
    fun setUp()
    fun onSwitchAutoChange(millisPeriod: Long)
    fun removeFavorite(favoriteId: Int)
    fun addFavorite(picture: Picture)
}

