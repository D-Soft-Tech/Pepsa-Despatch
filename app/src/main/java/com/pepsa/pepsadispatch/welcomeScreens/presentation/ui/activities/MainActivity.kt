package com.pepsa.pepsadispatch.welcomeScreens.presentation.ui.activities

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.root.viewTreeObserver.addOnPreDrawListener {
            true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideRight = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_X,
                    -splashScreenView.width.toFloat()
                )
                slideRight.apply {
                    duration = 600L
                    doOnEnd { splashScreenView.remove() }
                    start()
                }
            }
        }
    }
}
