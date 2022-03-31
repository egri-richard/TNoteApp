package com.tnote.tnoteapp.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnote.tnoteapp.api.ApiInstance
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.models.TTElement
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
    val timetableFragmentState: MutableLiveData<Resource<List<TTElement>>> = MutableLiveData()
    val ttElementFragmentState: MutableLiveData<Resource<TTElement>> = MutableLiveData()

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

    fun createNote(body: Note, token: String) = viewModelScope.launch {
        ApiInstance.api.newNote(token, body)
    }

    fun updateNote(noteId: Int, token: String, body: Note) = viewModelScope.launch {
        ApiInstance.api.updateNote(noteId, token, body)
    }

    fun deleteNote(noteId: Int, token: String) = viewModelScope.launch {
        ApiInstance.api.deleteNote(noteId, token)
    }

    fun getTimetables(userId: Int, token: String) = viewModelScope.launch {
        timetablesListFragmentState.postValue(Resource.Loading())
        val response = ApiInstance.api.getTimetables(userId, token)
        timetablesListFragmentState.postValue(handleTimetablesResponse(response))
    }

    fun getSelectedTimetable(timetableId: Int, token: String) = viewModelScope.launch {
        timetableFragmentState.postValue(Resource.Loading())
        val response = ApiInstance.api.getSelectedTimetable(timetableId, token)
        timetableFragmentState.postValue(handleSelectedTimetableResponse(response))
    }

    fun deleteTimetable(timetableId: Int, token: String) = viewModelScope.launch {
        ApiInstance.api.deleteTimetable(timetableId, token)
    }

    fun getSelectedTTElement(ttElementId: Int, token: String) = viewModelScope.launch {
        ttElementFragmentState.postValue(Resource.Loading())
        val response = ApiInstance.api.getSelectedTTElement(ttElementId, token)
        ttElementFragmentState.postValue(handleSelectedTTElementResponse(response))
    }

    fun createTTElement(token: String, body: TTElement) = viewModelScope.launch {
        ApiInstance.api.createTTElement(token, body)
    }

    fun updateTTElement(ttElementId: Int, token: String, body: TTElement) = viewModelScope.launch {
        ApiInstance.api.updateTTElement(ttElementId, token, body)
    }

    fun deleteTTElement(ttElementId: Int, token: String) = viewModelScope.launch {
        ApiInstance.api.deleteTTElement(ttElementId, token)
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

    private fun handleSelectedTimetableResponse(response: Response<List<TTElement>>): Resource<List<TTElement>> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    private fun handleSelectedTTElementResponse(response: Response<TTElement>) : Resource<TTElement> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    /*private fun handleSelectedNoteResponse(response: Response<Note>) : Resource<Note> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }*/


}