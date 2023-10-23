package dev.davron.regionaltaxidriver.fragment.login.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.davron.regionaltaxidriver.modelApi.loginActivity.signIn.signIn.ResponseSignIn
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullInformation
import dev.davron.regionaltaxidriver.repositories.NetworkRepository
import dev.davron.regionaltaxidriver.responseApis.ResApis
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PersonalInformationViewModel @Inject constructor(private val networkRepository: NetworkRepository) :
    ViewModel() {

    val requestFullInformationData = MutableLiveData<ResApis<ResponseSignIn>>(ResApis.Loading())

    fun requestFullInfo(requestFullInformation: RequestFullInformation) {
        viewModelScope.launch {
            try {
                networkRepository.requestFullInformation(requestFullInformation).also {
                    if (it.isSuccessful) {
                        requestFullInformationData.postValue(ResApis.Success(it.body()!!))
                    } else {
                        requestFullInformationData.postValue(
                            ResApis.Error(
                                it.errorBody()?.toString() ?: ""
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                requestFullInformationData.postValue(ResApis.Error(e.message ?: ""))
            }
        }
    }

}