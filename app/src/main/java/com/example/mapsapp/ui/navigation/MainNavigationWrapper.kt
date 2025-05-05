package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen
import com.example.mapsapp.ui.screens.PermissionsScreen


@Composable
fun MainNavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Destinations.PantallaPermisos) {

        composable<Destinations.PantallaPermisos>{
            PermissionsScreen { navController.navigate(Destinations.PantallaDrawer) }
        }

        composable<Destinations.PantallaDrawer>{
            DrawerScreen()
        }

    }
}