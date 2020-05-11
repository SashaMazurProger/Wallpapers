package com.sashamprog.wallpapers.base

import androidx.annotation.StringRes

/**
 * View
 */
interface MvpView<out T : MvpPresenter<*>> {

    fun showMessage(message: String?)

    fun showMessage(@StringRes resId: Int)

    val presenter: T
}