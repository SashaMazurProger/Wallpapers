package com.sashamprog.wallpapers.view.implement

import android.app.WallpaperManager
import android.os.Bundle
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.base.BaseActivity
import com.sashamprog.wallpapers.on
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_full_picture.*

class FullPictureActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_picture)

        intent.extras?.getString(FULL_URL_KEY)?.let { url ->
            Glide.with(this)
                .load(url)
                .into(image_view_picture)
        }

        button_set_as_wallpaper.setOnClickListener {
            Observable.fromCallable {
                val manager = WallpaperManager.getInstance(this@FullPictureActivity)
                try {
                    manager.setBitmap(image_view_picture.drawToBitmap())
                    button_set_as_wallpaper.isVisible = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
                .on()
                .subscribe({}, { error -> showMessage(error.message) })
        }
    }

    companion object {
        const val FULL_URL_KEY = "full_url_key"
    }
}
