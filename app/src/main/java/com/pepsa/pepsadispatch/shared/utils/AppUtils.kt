package com.pepsa.pepsadispatch.shared.utils

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.shared.utils.AppConstants.MAPS_KEY
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

object AppUtils {
    fun Fragment.changeStatusBarColor(@ColorRes colorId: Int) {
        val window: Window = this.requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), colorId)
    }

    fun Activity.changeStatusBarColor(@ColorRes colorId: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, colorId)
    }

    fun Fragment.getMapsKey(): String {
        val ai: ApplicationInfo = requireActivity().applicationContext.packageManager
            .getApplicationInfo(
                requireActivity().applicationContext.packageName,
                PackageManager.GET_META_DATA,
            )
        return ai.metaData[MAPS_KEY] as String
    }

    fun Activity.getMapsKey(): String {
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(
                applicationContext.packageName,
                PackageManager.GET_META_DATA,
            )
        return ai.metaData[MAPS_KEY] as String
    }

    fun Disposable.disposeWith(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(this)
    }

    fun Activity.showToast(@StringRes stringMessageId: Int) {
        Toast.makeText(this, getString(stringMessageId), Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showToast(@StringRes stringMessageId: Int) {
        Toast.makeText(requireContext(), getString(stringMessageId), Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showSnackBar(@StringRes stringMessageId: Int) {
        Snackbar.make(requireView().rootView, getString(stringMessageId), Snackbar.LENGTH_SHORT)
            .show()
    }

    fun Fragment.getLoaderDialog(): Dialog =
        Dialog(requireContext(), android.R.style.Theme_Light).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.loader_layout)
            setCancelable(false)
        }

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipBoard: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipBoard.setPrimaryClip(clip)
    }
}
