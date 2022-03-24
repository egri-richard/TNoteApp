package com.tnote.tnoteapp.logic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnote.tnoteapp.api.ApiInstance
import com.tnote.tnoteapp.models.RegistrationRequest
import com.tnote.tnoteapp.models.UserResponse
import com.tnote.tnoteapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    val registerActivityState: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    fun register(
        name: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
        registerActivityState.postValue(Resource.Loading())
        val response = ApiInstance.api.register(RegistrationRequest(name, email, password))
        registerActivityState.postValue(handleRegisterResponse(response))
    }

    private fun handleRegisterResponse(response: Response<UserResponse>): Resource<UserResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        Log.e("LoginError", "handleLoginResponse: ${response.errorBody()!!.charStream().readText()}", )
        return Resource.Error(response.message(), null)
    }
}