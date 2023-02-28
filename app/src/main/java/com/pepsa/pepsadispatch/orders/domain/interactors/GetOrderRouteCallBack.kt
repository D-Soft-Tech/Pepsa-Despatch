package com.pepsa.pepsadispatch.orders.domain.interactors

import com.pepsa.pepsadispatch.maps.domain.models.MapData

interface GetOrderRouteCallBack {
    fun onGetRouteSuccess(mapData: MapData?)
    fun onGetRouteError(errorMessage: String)
}