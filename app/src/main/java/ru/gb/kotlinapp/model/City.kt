package ru.gb.kotlinapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    // TODO city
    val city: String = "",
    val lat: Double = 0.00,
    val lon: Double = 0.00,
    val favorite: Boolean = false,
    val note: String = "",
    val region: String = ""

//    val city: String,
//    val lat: Double,
//    val lon: Double,
//    val favorite: Boolean,
//    val note: String,
//    val region: String
) : Parcelable