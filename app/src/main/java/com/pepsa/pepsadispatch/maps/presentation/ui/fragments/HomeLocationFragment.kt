package com.pepsa.pepsadispatch.maps.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.pepsa.pepsadispatch.maps.domain.usecases.MapsUseCase
import com.pepsa.pepsadispatch.maps.presentation.viewModels.MapViewModel
import com.pepsa.pepsadispatch.maps.utils.MapsConstants.MAP_ZOOM_14F
import com.pepsa.pepsadispatch.maps.utils.MapsConstants.MAP_ZOOM_18F
import com.pepsa.pepsadispatch.shared.utils.AppUtils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeLocationFragment :
    Fragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnPoiClickListener {
    private lateinit var binding: FragmentHomeLocationBinding
    private val viewModel: MapViewModel by viewModels()
    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapsUseCase: MapsUseCase

    @Inject
    lateinit var gson: Gson
    private lateinit var mMap: GoogleMap

    // GeeksforGeeks coordinates
    private var originLatitude: Double = 28.5021359
    private var originLongitude: Double = 77.4054901

    // Coordinates of a park nearby
    private var destinationLatitude: Double = 28.5151087
    private var destinationLongitude: Double = 77.3932163

    private val callback = OnMapReadyCallback { googleMap ->
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val origin = LatLng(originLatitude, originLongitude)
        val destination = LatLng(destinationLatitude, destinationLongitude)
        viewModel.getRoute(origin, destination)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home_location,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_map_type, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    mapsUseCase = MapsUseCase(mMap)
                    return when (menuItem.itemId) {
                        R.id.normal_view -> {
                            showToast(R.string.normal_view_selected)
                            mapsUseCase.selectNormalMapType()
                            true
                        }
                        R.id.satellite_view -> {
                            showToast(R.string.satellite_view_selected)
                            mapsUseCase.selectSatelliteMapType()
                            true
                        }
                        R.id.terrain_view -> {
                            showToast(R.string.terrain_view_selected)
                            mapsUseCase.selectTerrainMapType()
                            true
                        }
                        R.id.traffic_view -> {
                            showToast(R.string.traffic_view_selected)
                            mapsUseCase.showTraffic()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val originLocation = LatLng(originLatitude, originLongitude)
        mMap.apply {
            uiSettings.isMapToolbarEnabled = false
            clear()
            addMarker(MarkerOptions().position(originLocation))
            animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, MAP_ZOOM_18F))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.routePolylineOptions.observe(viewLifecycleOwner) { routePolyLine ->
            mapFragment?.getMapAsync { googleMap ->
                val originLocation = LatLng(originLatitude, originLongitude)
                val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
                mMap = googleMap.also {
                    it.addMarker(MarkerOptions().position(originLocation))
                    it.addMarker(
                        MarkerOptions().position(destinationLocation)
                            .title("Pepsa Foods and CO Limited"),
                    )
                    it.addPolyline(routePolyLine)
                    it.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            originLocation,
                            MAP_ZOOM_14F,
                        ),
                    )
                    it.uiSettings.isMapToolbarEnabled = false
                    it.setOnPoiClickListener(this)
                }
            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean = false
    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(
            requireContext(),
            poi.name,
            Toast.LENGTH_SHORT,
        ).show()
    }
}
