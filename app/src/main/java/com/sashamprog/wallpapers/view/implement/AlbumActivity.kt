package com.sashamprog.wallpapers.view.implement

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sashamprog.wallpapers.FavoriteManager
import com.sashamprog.wallpapers.FavoriteManagerImp
import com.sashamprog.wallpapers.R
import com.sashamprog.wallpapers.base.BaseActivity
import com.sashamprog.wallpapers.on
import com.sashamprog.wallpapers.ui_model.AlbumPicture
import com.sashamprog.wallpapers.ui_model.FavAlbum
import com.sashamprog.wallpapers.view.adapter.AlbumAdapter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_favorite.recycler_view

class AlbumActivity : BaseActivity() {
    private var adapter: AlbumAdapter? = null
    lateinit var favoriteManager: FavoriteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        favoriteManager = FavoriteManagerImp(this)

        button_create.setOnClickListener {
            favoriteManager.addAlbum(edit_text_name.text.toString())
                .on()
                .subscribe(
                    { newAlbum ->
                        adapter?.items?.add(newAlbum)
                        adapter?.notifyItemInserted(adapter!!.items.size - 1)
                        edit_text_name.setText("")
                    },
                    { error -> showMessage(error.message) }
                )
        }

        adapter = AlbumAdapter(
            mutableListOf(),
            mutableMapOf(),
            { _, _ -> },
            { view, album ->
                favoriteManager.removeAlbum(album.id)
                    .on()
                    .subscribe(
                        { result ->
                            if (result) {
                                showMessage("Removed")
                                val index = adapter!!.items.indexOf(album)
                                adapter!!.items.remove(album)
                                adapter!!.notifyItemRemoved(index)
                            } else {
                                showMessage("Some problem")
                            }
                        },
                        { error -> showMessage(error.message) }
                    )
            })

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        loadAlbums()
    }

    private fun loadAlbums() {
        class Merge(val albums: List<FavAlbum>, val albumPictures: List<AlbumPicture>)

        val biFunction = object : BiFunction<List<FavAlbum>, List<AlbumPicture>, Merge> {
            override fun apply(t1: List<FavAlbum>?, t2: List<AlbumPicture>?): Merge {
                return Merge(t1 ?: emptyList(), t2 ?: emptyList())
            }
        }

        Observable.zip(
            favoriteManager.albums(),
            favoriteManager.albumPictures(),
            biFunction
        )
            .on()
            .subscribe(
                { merge ->
                    val map = mutableMapOf<FavAlbum, List<AlbumPicture>>()
                    merge.albums.forEach { item ->
                        map[item] = merge.albumPictures.filter { it.albumId == item.id }
                    }
                    adapter?.items = merge.albums.toMutableList()
                    adapter?.albumPictures = map
                    adapter?.notifyDataSetChanged()
                },
                { error -> showMessage(error.message) }
            )
    }
}
