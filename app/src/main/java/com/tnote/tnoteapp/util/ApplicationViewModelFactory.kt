package com.tnote.tnoteapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tnote.tnoteapp.logic.ApplicationViewModel

class ApplicationViewModelFactory(
    val sessionManager: SessionManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApplicationViewModel(sessionManager) as T
    }
}