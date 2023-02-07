package com.pepsa.pepsadispatch.shared.utils

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pepsa.pepsadispatch.shared.utils.AppConstants.MAPS_KEY

object AppUtils {
    fun Fragment.changeStatusBarColor(@ColorRes colorId: Int) {
        val window: Window = this.requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), colorId)
    }

    fun Fragment.getMapsKey(): String {
        val ai: ApplicationInfo = requireActivity().applicationContext.packageManager
            .getApplicationInfo(
                requireActivity().applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        return ai.metaData[MAPS_KEY] as String
    }
}
