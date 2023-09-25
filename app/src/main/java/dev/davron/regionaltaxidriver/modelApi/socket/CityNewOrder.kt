package dev.davron.regionaltaxidriver.modelApi.socket

import java.io.Serializable

data class CityNewOrder(
    val activity: String,
    val cancel: String,
    val comments: String,
    val distance: String,
    val eta: String,
    val from: String,
    val from_location: String,
    val id: String,
    val payment_type: String,
    val status: String,
    val tariff: String
) : Serializable