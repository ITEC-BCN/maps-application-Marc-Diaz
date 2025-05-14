package com.example.mapsapp.data

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Marcador(
    val id: Int? = null,
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val latLng: String,
)