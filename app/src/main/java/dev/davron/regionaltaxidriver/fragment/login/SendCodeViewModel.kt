package dev.davron.regionaltaxidriver.fragment.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.davron.regionaltaxidriver.apiService.PhoneNumber
import dev.davron.regionaltaxidriver.repositories.NetworkRepository
import dev.davron.regionaltaxidriver.modelApi.loginActivity.ResCommon
import dev.davron.regionaltaxidriver.modelApi.loginActivity.signIn.signIn.ResSignIn
import dev.davron.regionaltaxidriver.modelApi.loginActivity.signIn.updatePhone.ResSendForUpdatePhone
import dev.davron.regionaltaxidriver.responseApis.ResApis
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendCodeViewModel @Inject constructor(private val networkRepository: NetworkRepository) :
    ViewModel() {

    val sendCodeMutableData = MutableLiveData<ResApis<ResCommon>>(ResApis.Loading())
    val signInMutableData = MutableLiveData<ResApis<ResSignIn>>(ResApis.Loading())

    fun sendCode(phoneNumber: PhoneNumber) {
        viewModelScope.launch {
            try {
                networkRepository.sendCode(phoneNumber).also {
                    if (it.isSuccessful) {
                        sendCodeMutableData.postValue(ResApis.Success(it.body()!!))
                    } else {
                        sendCodeMutableData.postValue(ResApis.Error(it.errorBody()?.string() ?: ""))
                    }
                }
            } catch (e: Exception) {
                sendCodeMutableData.postValue(ResApis.Error(e.message ?: ""))
            }
        }
    }

    fun signIn(phoneNumber: String, smsCode: String, fToken: String) {
        viewModelScope.launch {
            try {
                networkRepository.signIn(phoneNumber, smsCode, fToken).also {
                    if (it.isSuccessful) {
                        signInMutableData.postValue(ResApis.Success(it.body()!!))
                    } else {
                        signInMutableData.postValue(ResApis.Error(it.message()))
                    }
                }
            } catch (e: Exception) {
                signInMutableData.postValue(ResApis.Error(e.message ?: ""))
            }
        }
    }

    val sendSmsCodeUpdatePhone = MutableLiveData<ResApis<ResSendForUpdatePhone>>(ResApis.Loading())
    val verifyUpdatePhone = MutableLiveData<ResApis<ResCommon>>(ResApis.Loading())

    fun sendSmsCodeForUpdate(token: String, phone: String) = viewModelScope.launch {
        try {
            networkRepository.sendSmsUpdatePhone(token, phone).let {
                if (it.isSuccessful) {
                    sendSmsCodeUpdatePhone.postValue(ResApis.Success(it.body()!!))
                } else {
                    sendSmsCodeUpdatePhone.postValue(ResApis.Error(it.errorBody()?.string() ?: ""))
                }
            }
        } catch (e: Exception) {
            sendSmsCodeUpdatePhone.postValue(ResApis.Error(e.message ?: ""))
        }
    }

    fun verifyForUpdate(token: String, phone: String, code: String) = viewModelScope.launch {
        try {
            networkRepository.verifyUpdatePhone(token, phone, code).let {
                if (it.isSuccessful) {
                    verifyUpdatePhone.postValue(ResApis.Success(it.body()!!))
                } else {
                    verifyUpdatePhone.postValue(ResApis.Error(it.errorBody()?.string() ?: ""))
                }
            }
        } catch (e: Exception) {
            verifyUpdatePhone.postValue(ResApis.Error(e.message ?: ""))
        }
    }
}