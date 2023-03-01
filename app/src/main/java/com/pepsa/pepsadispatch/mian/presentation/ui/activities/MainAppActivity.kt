package com.pepsa.pepsadispatch.mian.presentation.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.ActivityMainAppBinding
import com.pepsa.pepsadispatch.maps.data.models.enums.AppDestinations.*
import com.pepsa.pepsadispatch.maps.presentation.viewModels.MapViewModel
import com.pepsa.pepsadispatch.orders.domain.models.OrderDomain
import com.pepsa.pepsadispatch.orders.presentation.viewModels.OrdersViewModel
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.STRING_INCOMING_ORDER_INTENT_ACTION
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.TAG_INCOMING_ORDER_RECEIVED
import com.pepsa.pepsadispatch.shared.utils.AppUtils.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainAppActivity : AppCompatActivity() {
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var binding: ActivityMainAppBinding

    @Inject
    lateinit var gson: Gson
    private val mapViewModel: MapViewModel by viewModels()
    private val orderViewModel: OrdersViewModel by viewModels()

    private lateinit var firebaseInstance: FirebaseMessaging
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        firebaseInstance = FirebaseMessaging.getInstance()
        changeStatusBarColor(R.color.primaryColor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_app)
        bottomNavBar = binding.bottomNavBar
        setSupportActionBar(binding.toolbar)

        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.utilites -> {
                    mapViewModel.setAppDestination(UTILITIES)
                    return@setOnItemSelectedListener true
                }
                R.id.tasks -> {
                    mapViewModel.setAppDestination(TASKS)
                    return@setOnItemSelectedListener true
                }
                R.id.wallet -> {
                    mapViewModel.setAppDestination(WALLET)
                    return@setOnItemSelectedListener true
                }
                R.id.profile_settings -> {
                    if (mapViewModel.appCurrentDestination.value != PROFILE_SETTINGS) {
                        mapViewModel.setAppDestination(PROFILE_SETTINGS)
                        navigateWithoutAction(R.id.destinationRouteFragment)
                    }
                    return@setOnItemSelectedListener true
                }
                else -> { // Set home as the default destination
                    if (mapViewModel.appCurrentDestination.value != HOME) {
                        mapViewModel.setAppDestination(HOME)
                        navigateWithoutAction(R.id.homeLocationFragment)
                    }
                    return@setOnItemSelectedListener true
                }
            }
        }
        getFirebaseDeviceToken(firebaseInstance) {
            Timber.d("INCOMING_TOKEN====>%s", it)
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNavBar.menu.getItem(2).isChecked = true
        listenForIntentOfIncomingOrder()
        if (!(
                intent != null && intent.action == STRING_INCOMING_ORDER_INTENT_ACTION &&
                    !(
                        intent.getStringExtra(
                            TAG_INCOMING_ORDER_RECEIVED,
                        ).isNullOrEmpty()
                        )
                )
        ) {
            navigateWithoutAction(R.id.homeLocationFragment)
        }
    }

    private fun getFirebaseDeviceToken(
        firebaseInstance: FirebaseMessaging,
        actionToPerformWithToken: (appToken: String) -> Unit,
    ) {
        firebaseInstance.token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val deviceToken = task.result
                    actionToPerformWithToken(deviceToken)
                }
            },
        )
    }

    private fun navigateWithoutAction(destinationId: Int) {
        findNavController(R.id.fragmentContainerView2).navigate(destinationId)
    }

    private fun listenForIntentOfIncomingOrder() {
        intent?.action?.let { incomingOrderAction ->
            intent.getStringExtra(TAG_INCOMING_ORDER_RECEIVED)
                .let { incomingOrder ->
                    if (incomingOrderAction == STRING_INCOMING_ORDER_INTENT_ACTION) {
                        incomingOrder?.let { pendingOrder ->
                            if (pendingOrder.isNotEmpty()) {
                                val order = gson.fromJson(pendingOrder, OrderDomain::class.java)
                                orderViewModel.setIncomingOrder(order)
                                navigateWithoutAction(R.id.homeLocationFragment)
                            }
                        }
                    }
                }
        }
    }
}
