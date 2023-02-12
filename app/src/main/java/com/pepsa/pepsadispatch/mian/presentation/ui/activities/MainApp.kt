package com.pepsa.pepsadispatch.mian.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.shared.utils.AppUtils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.primaryColor)
        setContentView(R.layout.activity_main_app)
    }
}
