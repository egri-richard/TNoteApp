package com.tnote.tnoteapp.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnote.tnoteapp.api.ApiInstance
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response

class ApplicationViewModel(sessionManager: SessionManager) : ViewModel() {
    val notesListFragmentState: MutableLiveData<Resource<List<Note>>> = MutableLiveData()

    init {
        getNotes(
            sessionManager.getUserId(),
            sessionManager.getAuthToken()
        )
    }

    fun getNotes(userId: Int, token: String) = viewModelScope.launch {
        notesListFragmentState.postValue(Resource.Loading())
        val response = ApiInstance.api.getNotes(userId, token)
        notesListFragmentState.postValue(handleNotesResponse(response))
    }

    private fun handleNotesResponse(response: Response<List<Note>>) : Resource<List<Note>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }
}