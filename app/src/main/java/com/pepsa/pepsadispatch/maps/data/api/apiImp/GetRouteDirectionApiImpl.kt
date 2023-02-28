package com.pepsa.pepsadispatch.maps.data.api.apiImp

import com.google.android.gms.maps.model.LatLng
import com.pepsa.pepsadispatch.maps.data.api.GetRouteDirectionApi
import com.pepsa.pepsadispatch.maps.domain.interactors.MapsApiRepository
import com.pepsa.pepsadispatch.maps.utils.MapUtils
import com.pepsa.pepsadispatch.shared.presentation.viewStates.ViewState
import com.pepsa.pepsadispatch.shared.utils.networkUtils.NetworkUtils
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRouteDirectionApiImpl @Inject constructor(
    private val mapUtils: MapUtils,
    private val getRouteDirectionApi: GetRouteDirectionApi,
    private val networkUtils: NetworkUtils,
) : MapsApiRepository {
    override fun getRoutePath(
        origin: String,
        destination: String,
        sensor: Boolean,
        mode: String,
        key: String,
    ): Single<ViewState<Pair<ArrayList<List<LatLng>>, Pair<Int, Int>>?>> =
        getRouteDirectionApi.getRouteDirection(origin, destination, sensor, mode, key)
            .flatMap {
                val result = networkUtils.getServerResponse(it).let { viewState ->
                    if (viewState.content != null) {
                        return@let ViewState(
                            content = mapUtils.mapMapDataToArrayListOfLatLng(
                                viewState.content,
                            ),
                            status = viewState.status,
                            message = viewState.message,
                        )
                    } else {
                        ViewState(
                            message = viewState.message,
                            status = viewState.status,
                            content = null,
                        )
                    }
                }
                Single.just(result)
            }
}
