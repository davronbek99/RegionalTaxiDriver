package dev.davron.regionaltaxidriver.repositories

import dev.davron.regionaltaxidriver.apiService.ApiService
import dev.davron.regionaltaxidriver.apiService.PhoneNumber
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val apiService: ApiService) {

    /** Authorization **/
    //sendcode
    suspend fun sendCode(phoneNumber: PhoneNumber) = apiService.sendCode(phoneNumber)

    //sign in
    suspend fun signIn(phoneNumber: String, smsCode: String, fToken: String) =
        apiService.signIn(phoneNumber, smsCode, fToken)


    suspend fun getActiveOrder(token: String) = apiService.getActiveOrder(token)


    suspend fun sendSmsUpdatePhone(token: String, phoneNumber: String) =
        apiService.sendSmsCodeForUpdate(token, phoneNumber)

    suspend fun verifyUpdatePhone(token: String, phoneNumber: String, code: String) =
        apiService.verifyUpdatePhone(token, phoneNumber, code)

}