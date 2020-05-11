package com.sashamprog.wallpapers.view.implement

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.sashamprog.wallpapers.FavoriteManager
import com.sashamprog.wallpapers.FavoriteManagerImp
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.base.BaseActivity
import com.sashamprog.wallpapers.on
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import com.sashamprog.wallpapers.view.adapter.FavoriteAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : BaseActivity() {
    private var adapter: FavoriteAdapter? = null
    lateinit var favoriteManager: FavoriteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteManager = FavoriteManagerImp(this)

        adapter = FavoriteAdapter(
            mutableListOf(),
            mutableListOf(),
            { _, _ -> },
            { view, favorite ->
                favoriteManager.removeFavorite(favorite.id)
                    .on()
                    .subscribe(
                        { result ->
                            if (result) {
                                showMessage("Unfavorite")
                                val index = adapter!!.items.indexOf(favorite)
                                adapter!!.items.remove(favorite)
                                adapter!!.notifyItemRemoved(index)
                            } else {
                                showMessage("Some problem")
                            }
                        },
                        { error -> showMessage(error.message) }
                    )
            },
            { view, favorite, album ->
                favoriteManager.addAlbumPicture(favorite.id, album.id)
                    .on()
                    .subscribe(
                        { result ->
                            if (result) {
                                showMessage("Success")
                            } else {
                                showMessage("Some problem")
                            }
                        },
                        { error -> showMessage(error.message) }
                    )
            })

        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = adapter

        loadPictures()
    }

    private fun loadPictures() {

        class Merge(val favorites: List<FavPicture>, val albums: List<FavAlbum>)

        val biFunction = object : BiFunction<List<FavPicture>, List<FavAlbum>, Merge> {
            override fun apply(t1: List<FavPicture>?, t2: List<FavAlbum>?): Merge {
                return Merge(t1 ?: emptyList(), t2 ?: emptyList())
            }
        }

        Observable.zip(
            favoriteManager.favorites(),
            favoriteManager.albums(),
            biFunction
        )
            .on()
            .subscribe(
                { merge ->
                    adapter?.items = merge.favorites.toMutableList()
                    adapter?.albums = merge.albums.toMutableList()
                    adapter?.notifyDataSetChanged()
                },
                { error -> showMessage(error.message) }
            )
    }
}
