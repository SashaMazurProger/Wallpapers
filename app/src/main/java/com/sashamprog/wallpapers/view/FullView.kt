package com.sashamprog.wallpapers.view

import android.graphics.Bitmap
import com.sashamprog.wallpapers.base.MvpView
import com.sashamprog.wallpapers.presenter.FullPresenter

interface FullView : MvpView<FullPresenter> {
    fun setBitmap(bitmap: Bitmap?)
}