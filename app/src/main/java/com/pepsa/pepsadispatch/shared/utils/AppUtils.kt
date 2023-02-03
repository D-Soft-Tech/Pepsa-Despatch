package com.pepsa.pepsadispatch.shared.utils

import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object AppUtils {
    fun Fragment.changeStatusBarColor(@ColorRes colorId: Int) {
        val window: Window = this.requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), colorId)
    }
}
