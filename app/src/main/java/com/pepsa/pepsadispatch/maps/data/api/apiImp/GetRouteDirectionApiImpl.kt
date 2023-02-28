package com.pepsa.pepsadispatch.maps.data.api.apiImp

import com.google.android.gms.maps.model.LatLng
import com.pepsa.pepsadispatch.maps.data.api.GetRouteDirectionApi
import com.pepsa.pepsadispatch.maps.domain.interactors.MapsApiRepository
import com.pepsa.pepsadispatch.maps.utils.MapUtils
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRouteDirectionApiImpl @Inject constructor(
    private val mapUtils: MapUtils,
    private val getRouteDirectionApi: GetRouteDirectionApi,
) : MapsApiRepository {
    override fun getRoutePath(
        origin: String,
        destination: String,
        sensor: Boolean,
        mode: String,
        key: String,
    ): Single<Pair<ArrayList<List<LatLng>>, Pair<Int, Int>>> =
        getRouteDirectionApi.getRouteDirection(origin, destination, sensor, mode, key)
            .flatMap {
                Single.just(mapUtils.mapMapDataToArrayListOfLatLng(it))
            }
}
