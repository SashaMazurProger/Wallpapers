package com.sashamprog.wallpapers.presenter.implement

import android.graphics.Bitmap
import com.sashamprog.wallpapers.Interactor
import com.sashamprog.wallpapers.base.BasePresenter
import com.sashamprog.wallpapers.presenter.FullPresenter
import com.sashamprog.wallpapers.view.FullView
import javax.inject.Inject

class FullPresenterImp @Inject constructor(
    private val interactor: Interactor
) : BasePresenter<FullView>(),
    FullPresenter {

    private var bitmap: Bitmap? = null

    override fun setUp(url: String) {
        interactor.loadBitmap(url)
            .lifecycle()
            .subscribe(
                { bitmap ->
                    this.bitmap = bitmap
                    mvpView?.setBitmap(bitmap)
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }

    override fun setBitmapAsWallpaper() {
        interactor.setWallpaper(bitmap!!)
            .lifecycle()
            .subscribe(
                { result ->
                    if (result) mvpView?.showMessage("Success")
                    else mvpView?.showMessage("Some problem")
                },
                { error -> mvpView?.showMessage(error.message) }
            )
    }
}