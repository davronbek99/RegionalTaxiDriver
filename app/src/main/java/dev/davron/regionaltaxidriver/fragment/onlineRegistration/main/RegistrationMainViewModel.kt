package dev.davron.regionaltaxidriver.fragment.onlineRegistration.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.davron.regionaltaxidriver.modelApi.mapTaxi.activeOrder.ResponseActiveOrder
import dev.davron.regionaltaxidriver.repositories.DatabaseRepository
import dev.davron.regionaltaxidriver.repositories.NetworkRepository
import dev.davron.regionaltaxidriver.responseApis.ResApis
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegistrationMainViewModel @Inject constructor(
    private val networkRepository: NetworkRepository, val databaseRepository: DatabaseRepository
) : ViewModel() {

    val activeOrder = MutableLiveData<ResApis<ResponseActiveOrder>>(ResApis.Loading())

    fun getActiveOrder(token: String) = viewModelScope.launch {
        try {
            networkRepository.getActiveOrder(token).let {
                if (it.isSuccessful) {
                    activeOrder.postValue(ResApis.Success(it.body()!!))
                } else {
                    activeOrder.postValue(ResApis.Error(it.errorBody()?.string() ?: ""))
                }
            }
        } catch (e: Exception) {
            activeOrder.postValue(ResApis.Error(e.message ?: ""))
        }
    }
}