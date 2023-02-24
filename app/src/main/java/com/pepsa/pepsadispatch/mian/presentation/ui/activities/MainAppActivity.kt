package com.pepsa.pepsadispatch.mian.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
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

        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.utilites -> {
                    return@setOnItemSelectedListener true
                }
                R.id.tasks -> {
                    return@setOnItemSelectedListener true
                }
                R.id.wallet -> {
                    return@setOnItemSelectedListener true
                }
                R.id.profile_settings -> {
                    navigateWithoutAction(R.id.destinationRouteFragment)
                    return@setOnItemSelectedListener true
                }
                else -> { // Set home as the default destination
                    navigateWithoutAction(R.id.homeLocationFragment)
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavBar.menu.getItem(2).isChecked = true
        navigateWithoutAction(R.id.homeLocationFragment)
    }

    private fun navigateWithoutAction(destinationId: Int) {
        findNavController(R.id.fragmentContainerView2).navigate(destinationId)
    }
}
