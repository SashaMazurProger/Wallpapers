package com.sashamprog.wallpapers.view

import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.base.MvpView
import com.sashamprog.wallpapers.presenter.MainPresenter

interface MainView : MvpView<MainPresenter> {
    fun showPictures(pictures: List<Picture>?)
    fun setAutoChangeSwitch(checked: Boolean)
}


