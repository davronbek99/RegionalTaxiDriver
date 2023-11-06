package dev.davron.regionaltaxidriver.models.attachUpload

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import java.io.Serializable

data class AttachUpload(
    @Part val file: MultipartBody.Part
) : Serializable
