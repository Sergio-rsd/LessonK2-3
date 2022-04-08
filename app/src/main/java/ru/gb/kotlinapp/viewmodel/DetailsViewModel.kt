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
    // TODO удалить
//    private var buttonClickFavoriteListener: DetailsFragment.ClickButtonFavoriteListener?
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
        val cityFavoriteClick = cityRepositoryImpl.getCityCondition(city)[0].favorite
        val cityLocal = City(city.city, city.lat, city.lat, !cityFavoriteClick, city.note)
        cityRepositoryImpl.updateCityCondition(cityLocal)
//        Log.d(TAG, "clickButtonFavorite() called with: city = $cityLocal")
        return !cityFavoriteClick
    }

    fun stateCityFavoriteNote(city: City): City {
        return cityRepositoryImpl.getCityCondition(city)[0]
    }

    fun updateCityNote(city: City, cityNote: String) {
        val cityLocal = City(city.city, city.lat, city.lat, city.favorite, cityNote)
        cityRepositoryImpl.updateCityCondition(cityLocal)
    }
}