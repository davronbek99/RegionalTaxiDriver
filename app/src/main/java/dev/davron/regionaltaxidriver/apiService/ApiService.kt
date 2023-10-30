package dev.davron.regionaltaxidriver.apiService

import dev.davron.regionaltaxidriver.modelApi.loginActivity.ResCommon
import dev.davron.regionaltaxidriver.modelApi.loginActivity.responseFullInformation.ResponseFullInformation
import dev.davron.regionaltaxidriver.modelApi.loginActivity.signIn.signIn.ResponseSignIn
import dev.davron.regionaltaxidriver.modelApi.loginActivity.signIn.updatePhone.ResSendForUpdatePhone
import dev.davron.regionaltaxidriver.modelApi.mapTaxi.activeOrder.ResponseActiveOrder
import dev.davron.regionaltaxidriver.modelApi.responseAttachUpload.ResponseAttachUpload
import dev.davron.regionaltaxidriver.models.attachUpload.AttachUpload
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullInformation
import dev.davron.regionaltaxidriver.models.signIn.SignIn
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {


    @POST("smstestiviy/send")
    @Headers("Content-Type: application/json")
    suspend fun sendCode(@Body phoneNumber: PhoneNumber): Response<ResCommon>


//    @FormUrlEncoded
//    @POST("smstestiviy/send")
//    @Headers("Content-Type: application/json")
//    suspend fun sendCode(@Field("phoneNumber") phoneNumber: String): Response<ResCommon>

    //sign in
//    @FormUrlEncoded
    @POST("smstestiviy/checkCode")
    @Headers("Content-Type: application/json")
    suspend fun signIn(
        @Body signIn: SignIn
    ): Response<ResponseSignIn>

    @POST("auth/registrationDriver")
    @Headers("Content-Type: application/json")
    suspend fun registrationDriver(
        @Body requestFullInformation: RequestFullInformation
    ): Response<ResponseFullInformation>

    @POST("attach/upload")
    @Headers("Content-Type: application/json")
    suspend fun attachUpload(
        @Body file: AttachUpload
    ): Response<ResponseAttachUpload>


    @GET("driver/orders/city/active-order")
    suspend fun getActiveOrder(
        @Header("Authorization") token: String
    ): Response<ResponseActiveOrder>

    @FormUrlEncoded
    @POST("driver/auth/update-phone/send-code")
    suspend fun sendSmsCodeForUpdate(
        @Header("Authorization") token: String,
        @Field("phone") phoneNumber: String,
    ): Response<ResSendForUpdatePhone>


    @FormUrlEncoded
    @POST("driver/auth/update-phone")
    suspend fun verifyUpdatePhone(
        @Header("Authorization") token: String,
        @Field("phone") phoneNumber: String,
        @Field("code") code: String
    ): Response<ResCommon>
}