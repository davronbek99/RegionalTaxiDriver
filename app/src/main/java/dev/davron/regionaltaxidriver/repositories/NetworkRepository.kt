package dev.davron.regionaltaxidriver.repositories

import dev.davron.regionaltaxidriver.apiService.ApiService
import dev.davron.regionaltaxidriver.apiService.PhoneNumber
import dev.davron.regionaltaxidriver.models.attachUpload.AttachUpload
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullInformation
import dev.davron.regionaltaxidriver.models.signIn.SignIn
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val apiService: ApiService) {

    /** Authorization **/
    //sendcode
    suspend fun sendCode(phoneNumber: PhoneNumber) = apiService.sendCode(phoneNumber)

    //sign in
    suspend fun signIn(signIn: SignIn) =
        apiService.signIn(signIn)

    suspend fun requestFullInformation(requestFullInformation: RequestFullInformation) =
        apiService.registrationDriver(requestFullInformation)

    suspend fun getActiveOrder(token: String) = apiService.getActiveOrder(token)


    suspend fun sendSmsUpdatePhone(token: String, phoneNumber: String) =
        apiService.sendSmsCodeForUpdate(token, phoneNumber)

    suspend fun verifyUpdatePhone(token: String, phoneNumber: String, code: String) =
        apiService.verifyUpdatePhone(token, phoneNumber, code)

    suspend fun attachUpload(file: AttachUpload) = apiService.attachUpload(file)
}