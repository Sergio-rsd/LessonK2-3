package ru.gb.kotlinapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.kotlinapp.App
import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Repository
import ru.gb.kotlinapp.model.RepositoryImpl
import ru.gb.kotlinapp.model.repository.room.city.LocalRepoCityImpl
import ru.gb.kotlinapp.util.REGION_RU
import ru.gb.kotlinapp.util.REGION_WORLD

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl(),
    private val cityRepoImpl: LocalRepoCityImpl = LocalRepoCityImpl(App.getHistoryDao())
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian)
//                        repositoryImpl.getWeatherFromLocalStorageRus()
                        cityRepoImpl.getCityRegion(REGION_RU)
                    else
//                        repositoryImpl.getWeatherFromLocalStorageWorld()
                        cityRepoImpl.getCityRegion(REGION_WORLD)
                )
            )
        }.start()
    }

    fun saveCityToEntity(city: City) {
        cityRepoImpl.saveCity(city)
    }
}