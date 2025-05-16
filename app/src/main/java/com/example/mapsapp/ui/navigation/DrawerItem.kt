package com.example.mapsapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val route: Destinations
) {
    HOME(Icons.Default.Home, "Mapa", Destinations.PantallaMapa),
    SETTINGS(Icons.Default.List, "Lista", Destinations.PantallaListaMarcador),
}
