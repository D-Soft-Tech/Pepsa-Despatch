package com.pepsa.pepsadispatch.orders.data.models // ktlint-disable filename

data class OrderEntity(
    val orderId: Long,
    val dateTime: String,
    val pickupStore: String,
    val receiverName: String,
    val address_1: String,
    val address_2: String,
    val landMark1: String,
    val landMark2: String,
    val taskPrice: String,
    val pickUpLongitude: Double,
    val pickUpLatitude: Double,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
    val pickupStorePhoneContact: String,
    val receiverPhoneNumber: String,
)
