package com.sashamprog.wallpapers.view.implement

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.base.BaseActivity
import com.sashamprog.wallpapers.presenter.implement.FavoritePresenterImp
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.ui_model.FavPicture
import com.sashamprog.wallpapers.view.FavoriteView
import com.sashamprog.wallpapers.view.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : BaseActivity(), FavoriteView {
    @Inject
    override lateinit var presenter: FavoritePresenterImp

    private var adapter: FavoriteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        presenter.onAttach(this)

        adapter = FavoriteAdapter(
            mutableListOf(),
            mutableListOf(),
            { _, _ -> },
            { view, favorite ->
                presenter.removeFavorite(favorite.id)
                val index = adapter!!.items.indexOf(favorite)
                adapter!!.items.remove(favorite)
                adapter!!.notifyItemRemoved(index)
            },
            { view, favorite, album ->
                presenter.addAlbumPicture(favorite.id, album.id)
            })

        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = adapter

        presenter.setUp()
    }

    override fun showPictures(favorites: List<FavPicture>, albums: List<FavAlbum>) {
        adapter?.items = favorites.toMutableList()
        adapter?.albums = albums.toMutableList()
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}
