package com.pepsa.pepsadispatch.mian.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.ActivityMainAppBinding
import com.pepsa.pepsadispatch.shared.utils.AppUtils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAppActivity : AppCompatActivity() {
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var binding: ActivityMainAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.primaryColor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_app)
        bottomNavBar = binding.bottomNavBar
        setSupportActionBar(binding.toolbar)
    }

    override fun onResume() {
        super.onResume()
        bottomNavBar.menu.getItem(2).isChecked = true
    }
}
