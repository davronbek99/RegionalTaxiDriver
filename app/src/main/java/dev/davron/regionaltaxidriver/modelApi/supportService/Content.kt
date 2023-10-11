package dev.davron.regionaltaxidriver.modelApi.supportService

data class Content(
    val address: String,
    val facebook: String,
    val instagram: String,
    val location: String,
    val phone_number: String,
    val telegram: String,
    val tg_account: String,
    val card: Boolean,
    val comission: Int,
    val driver_version: String
)