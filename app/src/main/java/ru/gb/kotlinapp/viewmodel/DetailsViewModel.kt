package ru.gb.kotlinapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.kotlinapp.App.Companion.getHistoryDao
import ru.gb.kotlinapp.model.City
import ru.gb.kotlinapp.model.Weather
import ru.gb.kotlinapp.model.WeatherDTO
import ru.gb.kotlinapp.model.repository.remote.DetailsRepository
import ru.gb.kotlinapp.model.repository.remote.DetailsRepositoryImpl
import ru.gb.kotlinapp.model.repository.remote.RemoteDataSource
import ru.gb.kotlinapp.model.repository.room.city.LocalRepoCityImpl
import ru.gb.kotlinapp.model.repository.room.history.LocalRepositoryImpl
import ru.gb.kotlinapp.util.*
import ru.gb.kotlinapp.view.details.DetailsFragment

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource()),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDao()),
    private val cityRepositoryImpl: LocalRepoCityImpl = LocalRepoCityImpl(getHistoryDao())
) : ViewModel(), DetailsFragment.ClickButtonFavoriteListener {

    private val callBack = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WeatherDTO): AppState {
            return if (checkDTOtoNull(serverResponse)) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(convertDtoToModel(serverResponse))
            }
        }
    }

    fun saveCityToDB(weather: Weather) {
        historyRepositoryImpl.saveEntity(weather)
    }

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    override fun clickButtonFavorite(city: City): Boolean {
//        val cityLocal = City(city.city, city.lat, city.lat, !cityFavoriteClick, city.note, city.region)
//            City(city.city, city.lat, city.lat, !cityFavoriteClick, city.note, city.region)
//        val cityFavoriteClick = getCityFavorite(city)

        val cityLocal = stateCityFavoriteNote(city)
        val cityFavoriteClick = cityLocal.favorite
        val cityUpdate = City(
            cityLocal.city,
            cityLocal.lat,
            cityLocal.lon,
            !cityFavoriteClick,
            cityLocal.note,
            cityLocal.region
        )
        cityRepositoryImpl.updateCityCondition(cityUpdate)
        return !cityFavoriteClick
    }

    fun getCityFavorite(city: City): Boolean {
        return cityRepositoryImpl.getCityCondition(city)[0].favorite
    }

    fun stateCityFavoriteNote(city: City): City {
        return cityRepositoryImpl.getCityCondition(city)[0]
    }

    fun updateCityFavoriteNote(city: City, favorite: Boolean, cityNote: String) {
        val cityLocal = stateCityFavoriteNote(city)
        val cityUpdate = City(
            cityLocal.city,
            cityLocal.lat,
            cityLocal.lon,
            favorite,
            cityNote,
            cityLocal.region
        )
//        val cityUpdate = City(city.city, city.lat, city.lat, favorite, cityNote, city.region)
        cityRepositoryImpl.updateCityCondition(cityUpdate)
    }
}