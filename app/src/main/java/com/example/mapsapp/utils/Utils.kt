package com.example.mapsapp.utils

import com.google.android.gms.maps.model.LatLng

fun stringToLatLng(str: String): LatLng{
    val lat = str.split(",").first().toDouble()
    val lang = str.split(",").last().toDouble()
    return LatLng(lat, lang)
}

fun latLangToString(latLng: LatLng): String{
    return "${latLng.latitude},${latLng.longitude}"
}