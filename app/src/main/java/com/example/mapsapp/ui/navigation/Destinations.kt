package com.example.mapsapp.ui.navigation

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed class Destinations{
    @Serializable
    object PantallaMapa: Destinations()

    @Serializable
    object PantallaListaMarcador: Destinations()

    @Serializable
    data class PantallaDetalleMarcador(val idMarcador: Int): Destinations()

    @Serializable
    data class PantallaCrearMarcador(val latLng: String): Destinations()

    @Serializable
    object PantallaPermisos: Destinations()

    @Serializable
    object PantallaDrawer: Destinations()

    @Serializable
    object PantallaAutenticacio : Destinations()
}