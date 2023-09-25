package dev.davron.regionaltaxidriver.modelApi.activeOrder.activeTaxiOrder

data class Content(
    val amount: String,
    val from: String,
    val from_latlng: String,
    val has_conditioner: Boolean,
    val has_overhead_luggage: Boolean,
    val id: Int,
    val order_type: String,
    val parcel_type: String,
    val picked_up: Boolean,
    val places: Places,
    val title: String
)