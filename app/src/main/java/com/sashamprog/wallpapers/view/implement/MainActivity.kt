package com.sashamprog.wallpapers.view.implement

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.transition.Scene
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sashamprog.wallpapers.Picture
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.base.BaseActivity
import com.sashamprog.wallpapers.presenter.MainPresenter
import com.sashamprog.wallpapers.view.MainView
import com.sashamprog.wallpapers.view.adapter.PictureAdapter
import com.sashamprog.wallpapers.worker.ChangeWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : BaseActivity(), MainView {
    @Inject
    override lateinit var presenter: MainPresenter

    private var pictureAdapter: PictureAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onAttach(this, this)
        presenter.setUp()

        bar_container.post {
            val alpha = ObjectAnimator.ofFloat(bar_container, "alpha", 0f, 1f)
            val scaleX = ObjectAnimator.ofFloat(bar_container, "scaleX", 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(bar_container, "scaleY", 0f, 1f)
            val transitionY = ObjectAnimator.ofFloat(
                bar_container,
                "translationY",
                (bar_container.height * -1).toFloat(),
                0f
            )
            val set = AnimatorSet()
            set.play(alpha)
                .with(scaleX)
                .with(scaleY)
                .with(transitionY)
            set.duration = 3000
            set.interpolator = DecelerateInterpolator()
            set.start()
        }

        switch_hide_favorite.post {
            val translateAnimation = TranslateAnimation(
                (switch_hide_favorite.width * -1).toFloat(),
                0f,
                0f,
                0f
            )
            translateAnimation.duration = 3000
            translateAnimation.interpolator = DecelerateInterpolator()
            switch_hide_favorite.startAnimation(translateAnimation)
        }

        ObjectAnimator.ofFloat(image_view_bar_icon, "alpha", 0f, 1f).apply {
            duration = 3000
            start()
        }

        ObjectAnimator.ofFloat(image_view_bar_icon, "rotation", 0f, 360f).apply {
            duration = 3000
            start()
        }

//        linear_layout1.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
//        Handler().postDelayed({
//            linear_layout1.removeViewAt(3)
//            linear_layout1.removeViewAt(5)
//        }, 3000)

//
//        TransitionManager.beginDelayedTransition(linear_layout2)
//        Handler().postDelayed({
//            linear_layout2.removeViewAt(1)
//            linear_layout2.removeViewAt(3)
//        }, 3000)

        val scene1 = Scene.getSceneForLayout(scene_root, R.layout.scene1, this)
        val scene2 = Scene.getSceneForLayout(scene_root, R.layout.scene2, this)
        switch_hide_favorite.setOnCheckedChangeListener { _, isChecked ->
            //            val set = TransitionSet()
//            set.addTransition(Fade())
//            set.addTransition(ChangeBounds())
//            set.ordering = TransitionSet.ORDERING_TOGETHER
//            set.duration = 1000
//            set.interpolator = AccelerateInterpolator()
            TransitionManager.go(if (isChecked) scene2 else scene1)
        }


        bar_container.setOnClickListener {
            val intent = Intent(this, IconAnimationActivity::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                val options = ActivityOptions.makeSceneTransitionAnimation(this, image_view_bar_icon, "icon")
                startActivity(intent, options.toBundle())
            } else {
                startActivity(intent)
            }
        }

        image_view_favorites.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intent)
        }

        image_view_albums.setOnClickListener {
            val intent = Intent(this@MainActivity, AlbumActivity::class.java)
            startActivity(intent)
        }

        button_start_change.setOnClickListener {
            presenter.onSwitchAutoChange()
        }

    }

    override fun setAutoChangeSwitch(checked: Boolean) {
        if (checked) {
            val minutesArray = resources.getStringArray(R.array.change_wallpaper_period_minutes)
            val minutes = minutesArray[spinner_period.selectedItemPosition].toLong()

            val request = PeriodicWorkRequestBuilder<ChangeWorker>(minutes, TimeUnit.MINUTES)
                .addTag("change_work")
                .build()

            WorkManager.getInstance(this)
                .enqueue(request)

            button_start_change.setText(R.string.button_stop_change)
        } else {
            WorkManager.getInstance(this)
                .cancelAllWorkByTag("change_work")

            button_start_change.setText(R.string.button_start_change)
        }
    }

    override fun showPictures(pictures: List<Picture>?) {
        recycler_view.layoutManager = GridLayoutManager(this@MainActivity, 2)
        pictureAdapter = PictureAdapter(pictures ?: emptyList(),
            { view, picture ->
                val intent = Intent(this@MainActivity, FullPictureActivity::class.java)
                intent.putExtra(FullPictureActivity.FULL_URL_KEY, picture.url)
                if (Build.VERSION.SDK_INT >= 21) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this@MainActivity,
                        view,
                        getString(R.string.transition_full_picture)
                    )
                    startActivity(intent, options.toBundle())
                } else {
                    startActivity(intent)
                }
            },
            { view, favorite ->
                if (favorite.isFavorite) {
                    presenter.removeFavorite(favorite.id)
                } else {
                    presenter.addFavorite(favorite)
                }
                favorite.isFavorite = !favorite.isFavorite
                pictureAdapter?.notifyItemChanged(
                    pictureAdapter!!.items.indexOf(
                        favorite
                    )
                )
            }
        )

        recycler_view.adapter = pictureAdapter
        pictureAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}

class MoveImageView : ImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawable?.bounds = Rect(x.toInt(), y.toInt(), (x + 3).toInt(), (y + 3).toInt())
            drawable?.draw(canvas)
            postInvalidateOnAnimation()
        }
    }
}