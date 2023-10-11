package dev.davron.regionaltaxidriver.models.login

import dev.davron.regionaltaxidriver.R


data class CountryCode(
    val flagResource: Int = R.drawable.ic_uzbekistan_flag,
    val code: String = "+998",
    val mask: String = "(__) ___-__-__",
    val name: String = "O'zbekiston"
)
