package dev.davron.regionaltaxidriver.models.login.fullName

import java.io.Serializable

data class RequestFullName(
     val firstName: String?,
     val lastName: String?,
     val birthday: String?,
     val gender: String?,
) : Serializable