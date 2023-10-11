package dev.davron.regionaltaxidriver.db

import androidx.room.*

@Dao
interface ApiRequestsDao {

    @Insert
    suspend fun insertNewRequest(model: ApiRequestDbModel)

    @Query("Select * from apirequestdbmodel where is_sent=:isSent")
    suspend fun getNotSentRequests(isSent: Boolean = false): List<ApiRequestDbModel>

    @Query("Select * from apirequestdbmodel")
    suspend fun getAllRequests(): List<ApiRequestDbModel>


    @Query("Update apirequestdbmodel set is_sent=:isSent where order_id=:order_id")
    suspend fun setRequestToSent(isSent: Boolean = true, order_id: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrder(model: OrderStatusModel)

    @Update
    suspend fun updateOrder(model: OrderStatusModel)

    @Query("select * from orderstatusmodel where order_id=:order_id and type=:type")
    suspend fun getSingleOrder(order_id: Int, type: String): OrderStatusModel?

    @Query("select * from apirequestdbmodel where order_id=:order_id and type=:type")
    suspend fun getSingleErrorCompleteOrder(order_id: Int, type: String): ApiRequestDbModel?

    @Query("select * from orderstatusmodel order by id desc limit 1")
    suspend fun getLastOrder(): OrderStatusModel

    @Query("delete from orderstatusmodel")
    suspend fun clearOrderStatusModelTable()

    @Query("delete from apirequestdbmodel")
    suspend fun clearApiReqestModelTable()
}