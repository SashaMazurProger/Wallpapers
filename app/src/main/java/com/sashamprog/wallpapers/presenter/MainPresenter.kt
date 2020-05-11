package com.sashamprog.wallpapers.presenter

import android.content.Context
import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.base.BasePresenter
import com.sashamprog.wallpapers.base.MvpPresenter
import com.sashamprog.wallpapers.view.MainView

interface MainPresenter : MvpPresenter<MainView> {
    fun onAttach(mvpView: MainView, context: Context)
    fun setUp()
    fun onSwitchAutoChange()
    fun removeFavorite(favoriteId: Int)
    fun addFavorite(picture: Picture)
}