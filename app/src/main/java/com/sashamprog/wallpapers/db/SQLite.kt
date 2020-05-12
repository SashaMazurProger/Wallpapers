package com.sashamprog.wallpapers.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sashamprog.wallpapers.Picture
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface DbHelper {
    fun favorites(): Observable<List<FavoriteDb>>
    fun addFavorite(favorite: Picture): Single<Boolean>
    fun removeFavorite(id: Int): Single<Boolean>

    fun addAlbum(name: String): Single<AlbumDb>
    fun removeAlbum(id: Int): Single<Boolean>
    fun albums(): Observable<List<AlbumDb>>
    fun albumPictures(): Observable<List<AlbumPictureDb>>
    fun removeAlbumPicture(pictureId: Int, albumId: Int): Single<Boolean>
    fun addAlbumPicture(pictureId: Int, albumId: Int): Single<Boolean>

    fun close()
}

class DbHelperImp @Inject constructor(
    context: Context
) : DbHelper, SQLiteOpenHelper(context, "app.db", null, 1) {

    private var db: SQLiteDatabase = writableDatabase

    override fun favorites(): Observable<List<FavoriteDb>> {
        return Observable.fromCallable {
            val items = mutableListOf<FavoriteDb>()
            val cursor = db.rawQuery("SELECT * FROM ${FavoriteDb.TABLE}", null)
            if (cursor.moveToFirst()) {
                do {
                    val item = FavoriteDb(
                        cursor.getInt(cursor.getColumnIndex(FavoriteDb.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(FavoriteDb.COLUMN_URL))
                    )
                    items.add(item)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return@fromCallable items
        }
    }

    override fun albums(): Observable<List<AlbumDb>> {
        return Observable.fromCallable {
            val items = mutableListOf<AlbumDb>()
            val cursor = db.rawQuery("SELECT * FROM ${AlbumDb.TABLE}", null)
            if (cursor.moveToFirst()) {
                do {
                    val item = AlbumDb(
                        cursor.getInt(cursor.getColumnIndex(AlbumDb.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AlbumDb.COLUMN_NAME))
                    )
                    items.add(item)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return@fromCallable items
        }
    }

    override fun albumPictures(): Observable<List<AlbumPictureDb>> {
        return Observable.fromCallable {
            val items = mutableListOf<AlbumPictureDb>()
            val cursor = db.rawQuery("SELECT * FROM ${AlbumPictureDb.TABLE}", null)
            if (cursor.moveToFirst()) {
                do {
                    val item = AlbumPictureDb(
                        cursor.getInt(cursor.getColumnIndex(AlbumPictureDb.COLUMN_PICTURE_ID)),
                        cursor.getInt(cursor.getColumnIndex(AlbumPictureDb.COLUMN_ALBUM_ID))
                    )
                    items.add(item)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return@fromCallable items
        }
    }

    override fun addFavorite(favorite: Picture): Single<Boolean> {
        return Single.fromCallable {
            val cv = ContentValues()
            cv.put(FavoriteDb.COLUMN_ID, favorite.id)
            cv.put(FavoriteDb.COLUMN_URL, favorite.url)
            return@fromCallable db.insert(FavoriteDb.TABLE, null, cv) != -1L
        }
    }

    override fun removeFavorite(id: Int): Single<Boolean> {
        return Single.fromCallable {
            return@fromCallable db.delete(
                FavoriteDb.TABLE,
                "${FavoriteDb.COLUMN_ID} = ?",
                arrayOf(id.toString())
            ) != 0
        }
    }

    override fun addAlbum(name: String): Single<AlbumDb> {
        return Single.fromCallable {
            val cv = ContentValues()
            cv.put(AlbumDb.COLUMN_NAME, name)
            val id = db.insert(AlbumDb.TABLE, null, cv)
            return@fromCallable if (id != -1L) AlbumDb(id.toInt(), name)
            else null
        }
    }

    override fun addAlbumPicture(pictureId: Int, albumId: Int): Single<Boolean> {
        return Single.fromCallable {
            val cv = ContentValues()
            cv.put(AlbumPictureDb.COLUMN_PICTURE_ID, pictureId)
            cv.put(AlbumPictureDb.COLUMN_ALBUM_ID, albumId)
            return@fromCallable db.insert(AlbumPictureDb.TABLE, null, cv) != -1L
        }
    }

    override fun removeAlbum(id: Int): Single<Boolean> {
        return Single.fromCallable {
            return@fromCallable db.delete(
                AlbumDb.TABLE,
                "${AlbumDb.COLUMN_ID} = ?",
                arrayOf(id.toString())
            ) != 0
        }
    }

    override fun removeAlbumPicture(pictureId: Int, albumId: Int): Single<Boolean> {
        return Single.fromCallable {
            return@fromCallable db.delete(
                AlbumPictureDb.TABLE,
                "${AlbumPictureDb.COLUMN_PICTURE_ID} = $pictureId AND ${AlbumPictureDb.COLUMN_ALBUM_ID} = $albumId",
                null
            ) != 0
        }
    }

    override fun close() {
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ${FavoriteDb.TABLE}(${FavoriteDb.COLUMN_ID} INTEGER PRIMARY KEY, ${FavoriteDb.COLUMN_URL} TEXT NOT NULL);")
        db?.execSQL("CREATE TABLE ${AlbumDb.TABLE}(${AlbumDb.COLUMN_ID} INTEGER PRIMARY KEY, ${AlbumDb.COLUMN_NAME} TEXT NOT NULL);")
        db?.execSQL("CREATE TABLE ${AlbumPictureDb.TABLE}(${AlbumPictureDb.COLUMN_PICTURE_ID} INTEGER, ${AlbumPictureDb.COLUMN_ALBUM_ID} INTEGER);")
    }
}

