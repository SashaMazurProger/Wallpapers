package com.sashamprog.wallpapers.base

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog : DialogFragment() {

    var baseActivity: BaseActivity? = null
        private set

    private var isDialogCancelable = true


    @TargetApi(23)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAttachToContext(context)
    }

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            onAttachToContext(activity)
    }

    private fun onAttachToContext(context: Context) {
        if (context is BaseActivity) {
            try {
                this.baseActivity = context
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    fun showMessage(message: String?) {
        baseActivity?.showMessage(message)
    }

    fun showMessage(@StringRes resId: Int) {
        baseActivity?.showMessage(resId)
    }

    protected abstract fun setUp(view: View)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the stringToSend
        val root = ConstraintLayout(requireContext())
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // creating the fullscreen dialog
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)

        if (dialog.window != null) {
//            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else {
            dismiss()
        }

        isCancelable = isDialogCancelable
        dialog.setCanceledOnTouchOutside(isDialogCancelable)

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(view)
    }

    fun show(fragmentManager: FragmentManager, tag: String, cancelable: Boolean) {
        this.isDialogCancelable = cancelable
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null)
            transaction.remove(prevFragment)
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

}