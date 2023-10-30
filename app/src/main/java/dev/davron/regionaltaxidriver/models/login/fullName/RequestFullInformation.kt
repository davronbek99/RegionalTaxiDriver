package dev.davron.regionaltaxidriver.models.login.fullName

data class RequestFullInformation(
    val name: String?,
    val surname: String?,
    val birthday: String?,
//    val gender: String?,
    val phoneNumber: String?,
    val driver_image: String
)
