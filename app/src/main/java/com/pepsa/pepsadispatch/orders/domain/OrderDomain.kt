package com.pepsa.pepsadispatch.orders.domain

import com.google.android.gms.maps.model.LatLng

data class OrderDomain(
    val orderId: Long,
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
)
