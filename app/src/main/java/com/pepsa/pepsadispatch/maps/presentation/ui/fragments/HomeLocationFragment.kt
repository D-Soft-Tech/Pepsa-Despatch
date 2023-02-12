package com.pepsa.pepsadispatch.maps.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.maps.presentation.viewModels.MapViewModel
import com.pepsa.pepsadispatch.maps.utils.MapsConstants.MAP_ZOOM_14F
import com.pepsa.pepsadispatch.maps.utils.MapsConstants.MAP_ZOOM_18F
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeLocationFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: MapViewModel by viewModels()
    private var mapFragment: SupportMapFragment? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val origin = LatLng(originLatitude, originLongitude)
        val destination = LatLng(destinationLatitude, destinationLongitude)
        viewModel.getRoute(origin, destination)
        return inflater.inflate(
            com.pepsa.pepsadispatch.R.layout.fragment_home_location,
            container,
            false,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val originLocation = LatLng(originLatitude, originLongitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, MAP_ZOOM_18F))
    }

    override fun onResume() {
        super.onResume()
        viewModel.routePolylineOptions.observe(viewLifecycleOwner) {
            mapFragment?.getMapAsync { googleMap ->
                mMap = googleMap
                val originLocation = LatLng(originLatitude, originLongitude)
                mMap.addMarker(MarkerOptions().position(originLocation))
                val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
                mMap.addMarker(MarkerOptions().position(destinationLocation))
                mMap.addPolyline(it)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, MAP_ZOOM_14F))
            }
        }
    }
}
