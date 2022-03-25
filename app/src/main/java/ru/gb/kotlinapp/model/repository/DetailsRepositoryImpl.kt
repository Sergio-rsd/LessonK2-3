package ru.gb.kotlinapp.model.repository

import retrofit2.Callback
import ru.gb.kotlinapp.model.WeatherDTO

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {
//    override fun getWeatherDetailsFromServer(lat: Double, lon: Double) {
//        TODO("Not yet implemented")
//    }

    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}