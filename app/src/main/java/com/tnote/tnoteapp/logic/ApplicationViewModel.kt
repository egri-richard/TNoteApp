package com.tnote.tnoteapp.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnote.tnoteapp.api.ApiInstance
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ApplicationViewModel: ViewModel() {
    val notesListFragmentState: MutableLiveData<Resource<List<Note>>> = MutableLiveData()

    init {
        //TODO: get user data and token into viewmodel somehow
        getNotes()
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