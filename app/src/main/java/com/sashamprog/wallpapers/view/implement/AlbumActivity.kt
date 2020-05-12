package com.sashamprog.wallpapers.view.implement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.base.BaseActivity
import com.sashamprog.wallpapers.presenter.AlbumPresenter
import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.view.AlbumView
import com.sashamprog.wallpapers.view.adapter.AlbumAdapter
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_favorite.recycler_view
import javax.inject.Inject

class AlbumActivity : BaseActivity(), AlbumView {

    @Inject
    override lateinit var presenter: AlbumPresenter
    private var adapter: AlbumAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        presenter.onAttach(this)

        button_create.setOnClickListener {
            presenter.addAlbum(edit_text_name.text.toString())
        }

        adapter = AlbumAdapter(
            mutableListOf(),
            mutableMapOf(),
            { _, _ -> },
            { view, album -> presenter.removeAlbum(album) })

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        presenter.setUp()
    }

    override fun onRemoveAlbumSuccess(albumId: Int) {
        val index = adapter!!.items.indexOf(
            adapter!!.items.find { it.id == albumId }
        )
        adapter!!.items.removeAt(index)
        adapter!!.notifyItemRemoved(index)
    }

    override fun onAddAlbumSuccess(newAlbum: FavAlbum?) {
        newAlbum?.let {
            adapter?.items?.add(newAlbum)
            adapter?.notifyItemInserted(adapter!!.items.size - 1)
            edit_text_name.setText("")
        }
    }

    override fun setAlbums(
        albums: List<FavAlbum>,
        albumPictures: List<AlbumPicture>
    ) {
        val map = mutableMapOf<FavAlbum, List<AlbumPicture>>()
        albums.forEach { item ->
            map[item] = albumPictures.filter { it.albumId == item.id }
        }
        adapter?.items = albums.toMutableList()
        adapter?.albumPictures = map
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}
