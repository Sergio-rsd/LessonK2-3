package ru.gb.kotlinapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.kotlinapp.App
import ru.gb.kotlinapp.model.repository.room.history.LocalRepositoryImpl

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(App.getHistoryDao())
) : ViewModel() {

    fun getAllHistoryQuery() {
        Thread {
            historyLiveData.postValue(AppState.Loading)
            historyLiveData.postValue(AppState.Success(historyRepositoryImpl.getAllHistory()))
        }.start()
    }
}