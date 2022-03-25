package com.tnote.tnoteapp.logic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnote.tnoteapp.api.ApiInstance
import com.tnote.tnoteapp.models.LoginRequest
import com.tnote.tnoteapp.models.UserResponse
import com.tnote.tnoteapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel: ViewModel() {
    val mainActivityState: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    fun login(email: String, password: String) = viewModelScope.launch {
        mainActivityState.postValue(Resource.Loading())
        val response = ApiInstance.api.login(LoginRequest(email, password))
        mainActivityState.postValue(handleLoginResponse(response))
    }

    private fun handleLoginResponse(response: Response<UserResponse>): Resource<UserResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        Log.e("LoginError", "handleLoginResponse: ${response.errorBody()!!.charStream().readText()}", )
        return Resource.Error(response.message(), null)
    }
}