package dev.davron.regionaltaxidriver.db

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderStatusModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var order_id: Int,
    var type: String,
    var status: String,
    var is_time_running: Boolean,
    var updated_time: Long,
    var last_location: String,
    var locations_list: String,
    var time: Int,
    var ride_time: Int,
    var diff_wait_time: Int,
    var wait_time: Int,
    var to_client_time: Int
)