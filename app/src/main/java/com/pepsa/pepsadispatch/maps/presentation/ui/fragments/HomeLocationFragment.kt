package com.pepsa.pepsadispatch.maps.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.gson.Gson
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.FragmentHomeLocationBinding
import com.pepsa.pepsadispatch.maps.domain.UiComponentUtils
import com.pepsa.pepsadispatch.maps.domain.usecases.MapsUseCase
import com.pepsa.pepsadispatch.maps.utils.MapsConstants
import com.pepsa.pepsadispatch.maps.utils.MapsMenuProvider
import com.pepsa.pepsadispatch.orders.presentation.ui.dialogs.IncomingOrderDialog
import com.pepsa.pepsadispatch.orders.presentation.viewModels.OrdersViewModel
import com.pepsa.pepsadispatch.orders.utils.DeliveryOrdersConstants.TAG_INCOMING_ORDER_DIALOG
import com.pepsa.pepsadispatch.shared.utils.BitmapGeneratorUtils.bitmapFromVector
import com.pepsa.pepsadispatch.shared.utils.ringer.RingerUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeLocationFragment :
    Fragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnPoiClickListener,
    UiComponentUtils {
    private lateinit var binding: FragmentHomeLocationBinding
    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapsUseCase: MapsUseCase
    private lateinit var menuProvider: MapsMenuProvider
    private val orderViewModel: OrdersViewModel by activityViewModels()

    @Inject
    lateinit var gson: Gson
    private lateinit var mMap: GoogleMap

    @Inject
    lateinit var ringerUtil: RingerUtil

    private var destinationLatitude: Double = 28.5151087
    private var destinationLongitude: Double = 77.3932163

    private val callback = OnMapReadyCallback { _ ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_location, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        mapFragment?.getMapAsync { googleMap ->
            val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            mMap = googleMap.also {
                it.addMarker(MarkerOptions().position(destinationLocation))?.apply {
                    title = getString(R.string.you)
                    setIcon(
                        bitmapFromVector(
                            requireActivity().applicationContext,
                            R.drawable.rider_location_icon,
                        ),
                    )
                }
                it.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        destinationLocation,
                        MapsConstants.MAP_ZOOM_14F,
                    ),
                )
                it.uiSettings.isMapToolbarEnabled = false
                it.setOnPoiClickListener(this)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val originLocation = LatLng(destinationLatitude, destinationLongitude)
        mMap.apply {
            uiSettings.isMapToolbarEnabled = false
            clear()
            addMarker(MarkerOptions().position(originLocation))?.apply {
                title = getString(R.string.you)
                setIcon(
                    bitmapFromVector(
                        requireActivity().applicationContext,
                        R.drawable.rider_location_icon,
                    ),
                )
            }
            animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    originLocation,
                    MapsConstants.MAP_ZOOM_14F,
                ),
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mapFragment?.getMapAsync {
            mMap = it
            mapsUseCase = MapsUseCase(mMap)
            menuProvider = MapsMenuProvider(mMap, mapsUseCase, this@HomeLocationFragment)
            requireActivity().addMenuProvider(
                menuProvider,
                viewLifecycleOwner,
                Lifecycle.State.RESUMED,
            )
        }
        orderViewModel.incomingOrder.observe(viewLifecycleOwner) {
            it?.let {
                val orderDialog = IncomingOrderDialog(ringerUtil)
                orderDialog.show(childFragmentManager, TAG_INCOMING_ORDER_DIALOG)
            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean = false

    override fun onPoiClick(p0: PointOfInterest) {
        Toast.makeText(requireContext(), p0.name, Toast.LENGTH_LONG).show()
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(requireContext(), getString(stringId), Toast.LENGTH_LONG).show()
    }
}
