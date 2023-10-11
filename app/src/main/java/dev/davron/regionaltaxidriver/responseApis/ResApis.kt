package dev.davron.regionaltaxidriver.responseApis

import dev.davron.regionaltaxidriver.db.ApiRequestDbModel
sealed class ResApis<T> {

    class Success<T>(val data: T) : ResApis<T>()

    class Error<T>(val message: String, val data: ApiRequestDbModel? = null) : ResApis<T>()

    class Loading<T> : ResApis<T>()
}