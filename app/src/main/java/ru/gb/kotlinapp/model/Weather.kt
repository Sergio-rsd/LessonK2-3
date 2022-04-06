package ru.gb.kotlinapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val weatherCondition: String = "",
    val precipitationStrength: Double = 0.00,
    val pressure: Int = 0,
    val pressureNormal: Int = 0,
    val humidity: Int = 0,
    val wind: Double = 0.00,
    val windDirection: String = "",
    val cloudiness: Double = 0.00,
    val sunrise: String = "00:00",
    val sunset: String = "00:00",
    val moon: String = ""
) : Parcelable

fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)

fun getWorldCities() = listOf(
    Weather(
        City("Лондон", 51.5085300, -0.1257400), 8, 7,
        "длительный сильный дождь", 11.0, 736, 744,
        100, 12.00, "З", 1.00, "08:12", "16:20",
        "убывающая луна"
    ),
    Weather(
        City("Токио", 35.6895000, 139.6917100), 13, 14,
        "облачно с прояснениями", 0.0, 748, 736,
        45, 3.00, "ЮВ", 0.35, "06:43", "18:45",
        "убывающая луна"
    ),
    Weather(
        City("Париж", 48.8534100, 2.3488000), 5, 6,
        "ясно", 0.0, 768, 750,
        41, 0.10, "СЗ", 0.00, "08:13", "17:55",
        "убывающая луна"
    ),
    Weather(
        City("Берлин", 52.52000659999999, 13.404953999999975), 7, 8,
        "пасмурно", 0.0, 754, 743,
        65, 3.50, "ЮЗ", 0.55, "07:43", "17:45",
        "убывающая луна"
    ),
    Weather(
        City("Нью-Йорк", 40.714606, -74.002800)
    ),
    Weather(
        City("Детройт", 42.331680, -83.046652)
    ),
    Weather(
        City("Буэнос-Айрес", -34.615697, -58.435104)
    )
)

fun getRussianCities() = listOf(
    Weather(
        City("Москва", 55.755826, 37.617299900000035), 1, 2,
        "дождь со снегом", 9.5, 739, 743,
        83, 6.00, "Ю", 1.0, "07:43", "17:45",
        "растущая луна"
    ),
    Weather(
        City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3,
        "небольшой дождь", 4.0, 730, 743,
        94, 6.2, "С", 0.95, "07:23", "17:45",
        "растущая луна"
    ),
    Weather(
        City("Новосибирск", 55.00835259999999, 82.93573270000002), -10, -12,
        "снегопад", 15.0, 729, 748,
        45, 0.7, "В", 1.00, "07:53", "16:45",
        "растущая луна"
    ),
    Weather(
        City("Екатеринбург", 56.83892609999999, 60.60570250000001), -7, -8,
        "небольшой снег", 8.5, 730, 741,
        51, 8.00, "СВ", 0.80, "07:48", "17:25",
        "растущая луна"
    ),
    Weather(
        City("Владивосток", 43.115542, 131.885494)
    ),
    Weather(
        City("Якутск", 62.027221, 129.732178)
    )
)

