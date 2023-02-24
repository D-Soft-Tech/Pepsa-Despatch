package com.pepsa.pepsadispatch.maps.utils

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.google.android.gms.maps.GoogleMap
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.maps.domain.UiComponentUtils
import com.pepsa.pepsadispatch.maps.domain.usecases.MapsUseCase

class MapsMenuProvider(
    private val mMap: GoogleMap,
    private var mapsUseCase: MapsUseCase,
    private val uiComponentUtils: UiComponentUtils,
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_map_type, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        mapsUseCase = MapsUseCase(mMap)
        return when (menuItem.itemId) {
            R.id.normal_view -> {
                uiComponentUtils.showToast(R.string.normal_view_selected)
                mapsUseCase.selectNormalMapType()
                true
            }
            R.id.satellite_view -> {
                uiComponentUtils.showToast(R.string.satellite_view_selected)
                mapsUseCase.selectSatelliteMapType()
                true
            }
            R.id.terrain_view -> {
                uiComponentUtils.showToast(R.string.terrain_view_selected)
                mapsUseCase.selectTerrainMapType()
                true
            }
            R.id.traffic_view -> {
                uiComponentUtils.showToast(R.string.traffic_view_selected)
                mapsUseCase.showTraffic()
                true
            }
            else -> false
        }
    }
}
