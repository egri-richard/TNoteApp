package com.tnote.tnoteapp.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnote.tnoteapp.api.ApiInstance
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.models.Timetable
import com.tnote.tnoteapp.models.User
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response

class ApplicationViewModel(sessionManager: SessionManager) : ViewModel() {
    val notesListFragmentState: MutableLiveData<Resource<List<Note>>> = MutableLiveData()
    val timetablesListFragmentState: MutableLiveData<Resource<List<Timetable>>> = MutableLiveData()
    val accountFragmentState: MutableLiveData<Resource<User>> = MutableLiveData()

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

    fun updateNote(noteId: Int, token: String, body: Note) = viewModelScope.launch {
        ApiInstance.api.updateNote(noteId, token, body)
    }

    fun getTimetables(userId: Int, token: String) = viewModelScope.launch {
        timetablesListFragmentState.postValue(Resource.Loading())
        val response = ApiInstance.api.getTimetables(userId, token)
        timetablesListFragmentState.postValue(handleTimetablesResponse(response))
    }

    fun getCurrentUser(userId: Int, token: String) = viewModelScope.launch {
        accountFragmentState.postValue(Resource.Loading())
        val response = ApiInstance.api.getUser(userId, token)
        accountFragmentState.postValue(handleCurrentUserResponse(response))
    }

    fun logout(token: String) = viewModelScope.launch {
        ApiInstance.api.logout(token)
    }

    private fun handleNotesResponse(response: Response<List<Note>>) : Resource<List<Note>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleTimetablesResponse(response: Response<List<Timetable>>) : Resource<List<Timetable>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleCurrentUserResponse(response: Response<User>) : Resource<User> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }
}