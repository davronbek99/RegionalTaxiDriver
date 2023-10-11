package dev.davron.regionaltaxidriver.repositories

import dev.davron.regionaltaxidriver.db.ApiRequestDbModel
import dev.davron.regionaltaxidriver.db.ApiRequestsDao
import dev.davron.regionaltaxidriver.db.OrderStatusModel
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val apiRequestsDao: ApiRequestsDao
) {
    suspend fun insertNewOrder(model: OrderStatusModel) = apiRequestsDao.insertNewOrder(model)

    suspend fun updateExistingOrder(model: OrderStatusModel) = apiRequestsDao.updateOrder(model)

    suspend fun getSingleOrder(orderId: Int, type: String) =
        apiRequestsDao.getSingleOrder(orderId, type)

    suspend fun insertNewRequest(model: ApiRequestDbModel) = apiRequestsDao.insertNewRequest(model)

    suspend fun getAllNotSentRequests() = apiRequestsDao.getNotSentRequests()

    suspend fun getAllRequests() = apiRequestsDao.getAllRequests()

    suspend fun setRequestToSent(orderId: Int) = apiRequestsDao.setRequestToSent(order_id = orderId)


    suspend fun getSingleRequest(orderId: Int, type: String) =
        apiRequestsDao.getSingleErrorCompleteOrder(orderId, type)

    suspend fun getLastOrder() = apiRequestsDao.getLastOrder()

    suspend fun clearTables() {
        apiRequestsDao.clearOrderStatusModelTable()
        apiRequestsDao.clearApiReqestModelTable()
    }
}