package dev.davron.regionaltaxidriver.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ApiRequestDbModel::class, OrderStatusModel::class], version = 2)
abstract class Database :RoomDatabase() {

    abstract fun apiRequestsDao(): ApiRequestsDao

    companion object {

        private var instance: dev.davron.regionaltaxidriver.db.Database? = null

        fun getDatabase(context: Context): dev.davron.regionaltaxidriver.db.Database {
            val tempInstance = instance

            if (tempInstance != null) {
                return tempInstance
            }

            instance =
                Room.databaseBuilder(
                    context,
                    dev.davron.regionaltaxidriver.db.Database::class.java,
                    "mana_taxi_db"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()


            return instance!!
        }
    }
}