package dev.davron.regionaltaxidriver.modelApi.activeOrder.activeTaxiOrder

data class ActiveTaxiOrderModel(
    val content: List<Content>,
    val message: Any,
    val success: Boolean
)