package com.pepsa.pepsadispatch.maps.domain.interactors

import com.google.android.gms.maps.model.LatLng
import com.pepsa.pepsadispatch.BuildConfig
import com.pepsa.pepsadispatch.maps.data.models.GetMapRouteMode
import com.pepsa.pepsadispatch.shared.presentation.viewStates.ViewState
import io.reactivex.Single

interface MapsApiRepository {
    fun getRoutePath(
        origin: String,
        destination: String,
        sensor: Boolean = false,
        mode: String = GetMapRouteMode.MODE_DRIVING.mode,
        key: String = BuildConfig.PLACE_API_KEY,
    ): Single<ViewState<Pair<ArrayList<List<LatLng>>, Pair<Int, Int>>?>>
}
