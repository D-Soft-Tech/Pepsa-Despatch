package com.pepsa.pepsadispatch.orders.utils.mappers

import com.google.android.gms.maps.model.LatLng
import com.pepsa.pepsadispatch.maps.domain.usecases.LocationUseCase
import com.pepsa.pepsadispatch.orders.data.models.OrderEntity
import com.pepsa.pepsadispatch.orders.domain.models.OrderDomain
import kotlin.reflect.full.memberProperties

object EntityMappers {
    private val locationUseCase: LocationUseCase = LocationUseCase()
    fun OrderEntity.toDomain(distance: Int, duration: Int, accepted: Boolean? = null): OrderDomain =
        with(::OrderDomain) {
            val propertiesByName = OrderEntity::class.memberProperties.associateBy { it.name }

            callBy(
                parameters.associateWith { parameter ->
                    when (parameter.name) {
                        OrderDomain::orderId.name -> orderId.toString()
                        OrderDomain::pickUpCoordinate.name -> LatLng(
                            pickUpLatitude,
                            pickUpLongitude,
                        )
                        OrderDomain::destinationCoordinate.name -> LatLng(
                            destinationLatitude,
                            destinationLongitude,
                        )
                        OrderDomain::distance.name -> distance.toString()
                        OrderDomain::timeTaken.name -> duration.toString()
                        OrderDomain::accepted.name -> accepted
                        else -> propertiesByName[parameter.name]?.get(this@toDomain)
                    }
                },
            )
        }
}
