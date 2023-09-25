package dev.davron.regionaltaxidriver.modelApi.activeOrder.activePostOrder

import dev.davron.regionaltaxidriver.modelApi.activeOrder.activeTaxiOrder.Places


data class Content(
    val amount: String,
    val from: String,
    val from_latlng: String,
    val has_overhead_luggage: Boolean,
    val id: Int,
    val order_type: String,
    val parcel_type: String,
    val picked_up: Boolean,
    val places: Places,
    val title: String
)