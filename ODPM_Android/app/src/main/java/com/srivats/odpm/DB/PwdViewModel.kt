package com.srivats.odpm.DB

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PwdViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<PwdItem>>
    private val repository: PwdRepository

    init {
        val todoDao = PwdDB.getInstance(application).pwdDao()
        repository = PwdRepository(todoDao)
        readAllData = repository.readAllData
    }

    fun addSitePwd(pwdItem: PwdItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSitePwd(pwdItem)
        }
    }

    fun updateSitePwd(pwdItem: PwdItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSitePwd(pwdItem = pwdItem)
        }
    }

    fun deleteSitePwd(pwdItem: PwdItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSitePwd(pwdItem = pwdItem)
        }
    }

    fun deleteAllSitePwd() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllSitePwd()
        }
    }
}

class PwdViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(PwdViewModel::class.java)) {
            return PwdViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}