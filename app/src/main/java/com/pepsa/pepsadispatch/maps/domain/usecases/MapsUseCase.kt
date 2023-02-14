package com.pepsa.pepsadispatch.maps.domain.usecases

import com.google.android.gms.maps.GoogleMap

class MapsUseCase(private val map: GoogleMap) {
    fun selectNormalMapType() {
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.isTrafficEnabled = false
    }

    fun selectSatelliteMapType() {
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        map.isTrafficEnabled = false
    }

    fun selectTerrainMapType() {
        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
        map.isTrafficEnabled = false
    }

    fun showTraffic() {
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.isTrafficEnabled = true
    }
}
