package com.sashamprog.wallpapers.base

/**
 * Presenter
 */
interface MvpPresenter<V> {
    fun onAttach(mvpView: V)
    fun onDetach()
}