package com.pepsa.pepsadispatch.mian.presentation.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.ActivityMainAppBinding
import com.pepsa.pepsadispatch.maps.data.models.enums.AppDestinations.*
import com.pepsa.pepsadispatch.maps.presentation.viewModels.MapViewModel
import com.pepsa.pepsadispatch.shared.utils.AppUtils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainAppActivity : AppCompatActivity() {
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var binding: ActivityMainAppBinding
    private val viewModel: MapViewModel by viewModels()

    @Inject
    lateinit var firebaseInstance: FirebaseMessaging
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.primaryColor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_app)
        bottomNavBar = binding.bottomNavBar
        setSupportActionBar(binding.toolbar)

        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.utilites -> {
                    viewModel.setAppDestination(UTILITIES)
                    return@setOnItemSelectedListener true
                }
                R.id.tasks -> {
                    viewModel.setAppDestination(TASKS)
                    return@setOnItemSelectedListener true
                }
                R.id.wallet -> {
                    viewModel.setAppDestination(WALLET)
                    return@setOnItemSelectedListener true
                }
                R.id.profile_settings -> {
                    if (viewModel.appCurrentDestination.value != PROFILE_SETTINGS) {
                        viewModel.setAppDestination(PROFILE_SETTINGS)
                        navigateWithoutAction(R.id.destinationRouteFragment)
                    }
                    return@setOnItemSelectedListener true
                }
                else -> { // Set home as the default destination
                    if (viewModel.appCurrentDestination.value != HOME) {
                        viewModel.setAppDestination(HOME)
                        navigateWithoutAction(R.id.homeLocationFragment)
                    }
                    return@setOnItemSelectedListener true
                }
            }
        }
        getFirebaseDeviceToken(firebaseInstance) {
            Timber.d("DEVICE_TOKEN===>%s", it)
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavBar.menu.getItem(2).isChecked = true
        navigateWithoutAction(R.id.homeLocationFragment)
    }

    private fun getFirebaseDeviceToken(
        firebaseInstance: FirebaseMessaging,
        actionToPerformWithToken: (appToken: String) -> Unit,
    ) {
        firebaseInstance.token.addOnCanceledListener {
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val deviceToken = task.result
                    actionToPerformWithToken(deviceToken)
                }
            }
        }
    }

    private fun navigateWithoutAction(destinationId: Int) {
        findNavController(R.id.fragmentContainerView2).navigate(destinationId)
    }
}
