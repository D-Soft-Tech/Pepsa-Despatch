package com.pepsa.pepsadispatch.maps.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.gson.Gson
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.FragmentHomeLocationBinding
import com.pepsa.pepsadispatch.maps.domain.usecases.MapsUseCase
import com.pepsa.pepsadispatch.maps.utils.MapsMenuProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeLocationFragment : Fragment() {
    private lateinit var binding: FragmentHomeLocationBinding
    private var mapFragment: SupportMapFragment? = null
    private lateinit var mapsUseCase: MapsUseCase
    private lateinit var menuProvider: MapsMenuProvider

    @Inject
    lateinit var gson: Gson
    private lateinit var mMap: GoogleMap

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
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_location, container, false)
        return binding.root
    }
}
