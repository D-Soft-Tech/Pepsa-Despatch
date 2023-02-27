package com.pepsa.pepsadispatch.orders.domain.models

import com.google.android.gms.maps.model.LatLng

data class OrderDomain(
    val orderId: String,
    val dateTime: String,
    val pickupStore: String,
    val receiverName: String,
    val address_1: String,
    val address_2: String,
    val landMark1: String,
    val landMark2: String,
    val taskPrice: String,
    val pickUpCoordinate: LatLng,
    val destinationCoordinate: LatLng,
    val pickupStorePhoneContact: String,
    val receiverPhoneNumber: String,
    val distance: String,
    val timeTaken: String,
    var accepted: Boolean? = null
)
