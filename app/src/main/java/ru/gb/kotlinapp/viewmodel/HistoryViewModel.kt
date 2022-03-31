package ru.gb.kotlinapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.kotlinapp.App
import ru.gb.kotlinapp.model.repository.LocalRepositoryImpl

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(App.getHistoryDao())
) : ViewModel() {

    // TODO -> потоки!!!!!!
    fun getAllHistoryQuery() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.Success(historyRepositoryImpl.getAllHistory())
    }
}