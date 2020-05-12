package com.sashamprog.wallpapers.base

import androidx.core.util.Consumer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


open class BasePresenter<V> : MvpPresenter<V> {

    private val disposables = CompositeDisposable()

    protected fun <T> Observable<T>.lifecycle(): Observable<T> {
        return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { disposable -> disposables.add(disposable) }
    }

    protected fun <T> Single<T>.lifecycle(): Single<T> {
        return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { disposable -> disposables.add(disposable) }
    }

    var mvpView: V? = null
        private set

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun onDetach() {
        disposables.clear()
        mvpView = null
    }
}