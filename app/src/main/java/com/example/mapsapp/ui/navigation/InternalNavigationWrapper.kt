package com.example.mapsapp.ui.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.AuthScreen
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DetailMarkerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen
import com.example.mapsapp.utils.latLangToString

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, Destinations.PantallaMapa) {
        composable<Destinations.PantallaMapa> {
            MapScreen({ latLng ->
                navController.navigate(Destinations.PantallaCrearMarcador(latLangToString(latLng)))
            }){ marcadorId ->
                navController.navigate(Destinations.PantallaDetalleMarcador(marcadorId))
        }
        }
        composable<Destinations.PantallaCrearMarcador> {
            backStackEntry ->
            val pantallaCrearMarcador = backStackEntry.toRoute<Destinations.PantallaCrearMarcador>()
            CreateMarkerScreen(pantallaCrearMarcador.latLng){
                navController.popBackStack()
            }
        }
        composable<Destinations.PantallaListaMarcador> {
            Log.d("LLega a aqui", "Navegacion Lista")
            MarkerListScreen(modifier){
                marcadorId ->
                navController.navigate(Destinations.PantallaDetalleMarcador(marcadorId))
            }
        }

        composable<Destinations.PantallaDetalleMarcador> {
                backStackEntry ->
            val pantallaDetalleMarcador = backStackEntry.toRoute<Destinations.PantallaDetalleMarcador>()
            DetailMarkerScreen(pantallaDetalleMarcador.idMarcador){
                navController.popBackStack()
            }
        }

        composable<Destinations.PantallaAutenticacio> {
            MainNavigationWrapper(true)
        }
    }
}

