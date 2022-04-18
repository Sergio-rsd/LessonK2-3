package ru.gb.kotlinapp.model

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
//    override fun getWeatherFromLocalStorageRus() = getAllCity()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}