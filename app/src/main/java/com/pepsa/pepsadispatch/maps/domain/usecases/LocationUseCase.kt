package com.pepsa.pepsadispatch.maps.domain.usecases

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationUseCase @Inject constructor() {

    fun getDistance(
        startCoordinate: LatLng,
        endCoordinate: LatLng,
    ): String {
        val resultHolder = FloatArray(10)
        Location.distanceBetween(
            startCoordinate.latitude,
            startCoordinate.longitude,
            endCoordinate.latitude,
            endCoordinate.longitude,
            resultHolder,
        )
        return String.format("%.1f", resultHolder[0] / 1000)
    }
}
