package com.sashamprog.wallpapers.view.implement

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.base.BaseActivity
import com.sashamprog.wallpapers.on
import com.sashamprog.wallpapers.presenter.FullPresenter
import com.sashamprog.wallpapers.view.FullView
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_full_picture.*
import javax.inject.Inject

class FullPictureActivity : BaseActivity(), FullView {

    @Inject
    override lateinit var presenter: FullPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_picture)
        presenter.onAttach(this)

        button_set_as_wallpaper.isVisible = false

        button_set_as_wallpaper.setOnClickListener {
            presenter.setBitmapAsWallpaper()
        }

        intent.extras?.getString(FULL_URL_KEY)?.let { url ->
            presenter.setUp(url)
        }
    }

    override fun setBitmap(bitmap: Bitmap?) {
        image_view_picture.setImageBitmap(bitmap)
        button_set_as_wallpaper.isVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    companion object {
        const val FULL_URL_KEY = "full_url_key"
    }
}
