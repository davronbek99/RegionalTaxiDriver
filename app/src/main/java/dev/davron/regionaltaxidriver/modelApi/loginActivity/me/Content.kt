package dev.davron.regionaltaxidriver.modelApi.loginActivity.me

data class Content(
    val id: Int,
    val activity: Int,
    val driver_id: Int,
    val city_mode_enabled: Boolean,
    val date_of_birth: String,
    val document_type: String,
    val driver_license: String?,
    val driver_license_expiration: String?,
    var driver_license_photo1: String?,
    var driver_license_photo2: String?,
    var driver_license_photo3: String?,
    val gender: String,
    val name: String,
    var passport_copy1: String?,
    var passport_copy2: String?,
    var has_sticker: Boolean,
    var passport_copy3: String?,
    val passport_serial: String?,
    val phone: String,
    val photo: String?,
    val status: String?,
    val surname: String,
    val rating: String?,
    val balance: Int
)