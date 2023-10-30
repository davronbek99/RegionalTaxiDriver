package dev.davron.regionaltaxidriver.fragment.login.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.davron.regionaltaxidriver.modelApi.loginActivity.responseFullInformation.ResponseFullInformation
import dev.davron.regionaltaxidriver.modelApi.responseAttachUpload.ResponseAttachUpload
import dev.davron.regionaltaxidriver.models.attachUpload.AttachUpload
import dev.davron.regionaltaxidriver.models.login.fullName.RequestFullInformation
import dev.davron.regionaltaxidriver.repositories.NetworkRepository
import dev.davron.regionaltaxidriver.responseApis.ResApis
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class PersonalInformationViewModel @Inject constructor(private val networkRepository: NetworkRepository) :
    ViewModel() {

    val requestFullInformationData =
        MutableLiveData<ResApis<ResponseFullInformation>>(ResApis.Loading())
    val attachUploadData = MutableLiveData<ResApis<ResponseAttachUpload>>(ResApis.Loading())
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

    fun attachUpload(file: AttachUpload) {
        viewModelScope.launch {
            try {
                networkRepository.attachUpload(file).also {
                    if (it.isSuccessful) {
                        attachUploadData.postValue(ResApis.Success(it.body()!!))
                    } else {
                        attachUploadData.postValue(ResApis.Error(it.errorBody()?.toString() ?: ""))
                    }
                }
            } catch (e: Exception) {
                attachUploadData.postValue(ResApis.Error(e.message ?: ""))
            }
        }
    }

}