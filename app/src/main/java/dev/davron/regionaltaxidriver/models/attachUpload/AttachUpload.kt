package dev.davron.regionaltaxidriver.models.attachUpload

import okhttp3.MultipartBody

data class AttachUpload(
    val file: MultipartBody.Part
)
