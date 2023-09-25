package dev.davron.regionaltaxidriver.modelApi.activeOrder.activePostOrder

data class OrderPostActiveModel(
    val content: List<Content>,
    val message: Any,
    val success: Boolean
)