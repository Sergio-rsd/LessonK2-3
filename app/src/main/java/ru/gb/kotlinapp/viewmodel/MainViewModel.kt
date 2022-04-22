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
    // TODO city

//    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val liveDataToObserve: MutableLiveData<AppStateCity> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl(),
    private val cityRepoImpl: LocalRepoCityImpl = LocalRepoCityImpl(App.getHistoryDao())
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

//    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceRus() =
        getDataFromLocalSource(isRussian = true, isFavorite = false)

    fun getWeatherFromLocalSourceRusFavorite() =
        getDataFromLocalSource(isRussian = true, isFavorite = true)

    fun getWeatherFromLocalSourceWorld() =
        getDataFromLocalSource(isRussian = false, isFavorite = false)

    fun getWeatherFromLocalSourceWorldFavorite() =
        getDataFromLocalSource(isRussian = false, isFavorite = true)

    private fun getDataFromLocalSource(isRussian: Boolean, isFavorite: Boolean) {
//        liveDataToObserve.value = AppState.Loading
        liveDataToObserve.value = AppStateCity.LoadingCity
        Thread {
            liveDataToObserve.postValue(
                // TODO city

//                AppState.Success(
                AppStateCity.SuccessCity(
                    if (isRussian)
                        if (isFavorite)
                        // TODO city
                            
//                            cityRepoImpl.getCityRegionFavorite(REGION_RU)
                            cityRepoImpl.getCityRegionFavoriteMain(REGION_RU)
                        else
//                            cityRepoImpl.getCityRegion(REGION_RU)
                            cityRepoImpl.getCityRegionMain(REGION_RU)
                    else
                        if (isFavorite)
//                            cityRepoImpl.getCityRegionFavorite(REGION_WORLD)
                            cityRepoImpl.getCityRegionFavoriteMain(REGION_WORLD)
                        else
//                            cityRepoImpl.getCityRegion(REGION_WORLD)
                            cityRepoImpl.getCityRegionMain(REGION_WORLD)
                    // TODO city
                )
            )
        }.start()
    }

    fun saveCityToEntity(city: City) {
        cityRepoImpl.saveCity(city)
    }
}