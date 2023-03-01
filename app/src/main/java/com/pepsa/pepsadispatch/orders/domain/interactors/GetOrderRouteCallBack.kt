package com.pepsa.pepsadispatch.orders.domain.interactors

import androidx.work.WorkInfo
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity

interface GetOrderRouteCallBack {
    fun onGetRouteSuccess(workInfo: WorkInfo?, orderEntity: OrderEntity)
    fun onGetRouteError(errorMessage: String)
}
