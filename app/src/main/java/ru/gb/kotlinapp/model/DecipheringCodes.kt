package ru.gb.kotlinapp.model

fun getCondition() = mapOf(
    "clear" to "ясно",
    "partly-cloudy" to "малооблачно",
    "cloudy" to "облачно с прояснениями",
    "overcast" to "пасмурно",
    "drizzle" to "морось",
    "light-rain" to "небольшой дождь",
    "rain" to "дождь",
    "moderate-rain" to "умеренно сильный дождь",
    "heavy-rain" to "сильный дождь",
    "continuous-heavy-rain" to "длительный сильный дождь",
    "showers" to "ливень",
    "wet-snow" to "дождь со снегом",
    "light-snow" to "небольшой снег",
    "snow" to "снег",
    "snow-showers" to "снегопад",
    "hail" to "град",
    "thunderstorm" to "гроза",
    "thunderstorm-with-rain" to "дождь с грозой",
    "thunderstorm-with-hail" to "гроза с градом"
)

fun getWindDirection() = mapOf(
    "nw" to "СЗ",
    "n" to "С",
    "ne" to "СВ",
    "e" to "В",
    "se" to "ЮВ",
    "s" to "Ю",
    "sw" to "ЮЗ",
    "w" to "З",
    "c" to "штиль"
)

fun getMoonCondition() = mapOf(
    "moon-code-0" to "полнолуние",
    "moon-code-1" to "убывающая луна",
    "moon-code-2" to "убывающая луна",
    "moon-code-3" to "убывающая луна",
    "moon-code-4" to "последняя четверть",
    "moon-code-5" to "убывающая луна",
    "moon-code-6" to "убывающая луна",
    "moon-code-7" to "убывающая луна",
    "moon-code-8" to "новолуние",
    "moon-code-9" to "растущая луна",
    "moon-code-10" to "растущая луна",
    "moon-code-11" to "растущая луна",
    "moon-code-12" to "первая четверть",
    "moon-code-13" to "растущая луна",
    "moon-code-14" to "растущая луна",
    "moon-code-15" to "растущая луна"
)