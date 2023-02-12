package com.pepsa.pepsadispatch.maps.utils

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.pepsa.pepsadispatch.maps.domain.models.MapData
import com.pepsa.pepsadispatch.shared.utils.AppConstants.MAP_LINE_WIDTH_10
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapUtils @Inject constructor(private val gson: Gson) {
    fun mapMapDataToArrayListOfLatLng(respObj: MapData): ArrayList<List<LatLng>> {
        val result = ArrayList<List<LatLng>>()
        try {
            val path = ArrayList<LatLng>()
            for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
            }
            result.add(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    fun convertRouteFromLatLngToLineOption(route: ArrayList<List<LatLng>>): PolylineOptions {
        val lineOption = PolylineOptions()
        route.forEach { latLng ->
            lineOption.apply {
                addAll(latLng)
                width(MAP_LINE_WIDTH_10)
                color(Color.GREEN)
                geodesic(true)
            }
        }
        return lineOption
    }
}