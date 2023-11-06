package dev.davron.regionaltaxidriver.fragment.onlineRegistration.onlineRegistrationViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.davron.regionaltaxidriver.modelApi.loginActivity.ResCommon
import dev.davron.regionaltaxidriver.modelApi.responseAttachUpload.ResponseAttachUpload
import dev.davron.regionaltaxidriver.models.attachUpload.AttachUpload
import dev.davron.regionaltaxidriver.repositories.NetworkRepository
import dev.davron.regionaltaxidriver.responseApis.ResApis
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class OnlineRegistrationViewModel @Inject constructor(private val networkRepository: NetworkRepository) :
    ViewModel() {

    val passportSerialMutableData = MutableLiveData<ResApis<ResCommon>>(ResApis.Loading())
    val passportPhoto1 = MutableLiveData<ResApis<ResCommon>>(ResApis.Loading())
    val passportPhoto2 = MutableLiveData<ResApis<ResCommon>>(ResApis.Loading())
    val passportPhoto3 = MutableLiveData<ResApis<ResCommon>>(ResApis.Loading())
    val attachUploadData = MutableLiveData<ResApis<ResponseAttachUpload>>(ResApis.Loading())

    fun attachUpload(file: RequestBody) {
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

    fun setPassportSerial(token: String, documentType: String, passportSerial: String) =
        viewModelScope.launch {
            try {
                networkRepository.setPassportSerial(token, documentType, passportSerial).also {
                    if (it.isSuccessful) {
                        passportSerialMutableData.postValue(ResApis.Success(it.body()!!))
                    } else {
                        passportSerialMutableData.postValue(
                            ResApis.Error(
                                it.errorBody()?.string() ?: ""
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                passportSerialMutableData.postValue(ResApis.Error(e.message ?: ""))
            }
        }

    fun setPassportPhoto1(token: String, passportPhoto: String) = viewModelScope.launch {
        try {
            networkRepository.setPassportPhoto1(token, passportPhoto).also {
                if (it.isSuccessful) {
                    passportPhoto1.postValue(ResApis.Success(it.body()!!))
                } else {
                    passportPhoto1.postValue(ResApis.Error(it.errorBody()?.string() ?: ""))
                }
            }
        } catch (e: Exception) {
            passportPhoto1.postValue(ResApis.Error(e.message ?: ""))
        }
    }

    fun setPassportPhoto2(token: String, passportPhoto: String) = viewModelScope.launch {
        try {
            networkRepository.setPassportPhoto2(token, passportPhoto).also {
                if (it.isSuccessful) {
                    passportPhoto2.postValue(ResApis.Success(it.body()!!))
                } else {
                    passportPhoto2.postValue(ResApis.Error(it.errorBody()?.string() ?: ""))
                }
            }
        } catch (e: Exception) {
            passportPhoto2.postValue(ResApis.Error(e.message ?: ""))
        }
    }

    fun setPassportPhoto3(token: String, passportPhoto: String) = viewModelScope.launch {
        try {
            networkRepository.setPassportPhoto3(token, passportPhoto).also {
                if (it.isSuccessful) {
                    passportPhoto3.postValue(ResApis.Success(it.body()!!))
                } else {
                    passportPhoto3.postValue(ResApis.Error(it.errorBody()?.string() ?: ""))
                }
            }
        } catch (e: Exception) {
            passportPhoto3.postValue(ResApis.Error(e.message ?: ""))
        }
    }
}