package dev.davron.regionaltaxidriver.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ApiRequestDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val order_id: Int,
    val type: String,
    val ride_time: Int,
    val ride_distance: Int,
    val wait_time: Int,
    val wait_time_amount: Int,
    val ride_amount: Int,
    val is_sent: Boolean = false,
    val lastLocation: String,
    val lastAddress: String,
    val districtId: Int
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApiRequestDbModel

        if (order_id != other.order_id) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = order_id
        result = 31 * result + type.hashCode()
        return result
    }
}