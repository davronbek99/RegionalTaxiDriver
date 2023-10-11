package dev.davron.regionaltaxidriver.models.permissionRequest

data class PermissionModel(
    val name: String,
    val description: String,
    val scheme: String,
    var granted: Boolean = false
)
