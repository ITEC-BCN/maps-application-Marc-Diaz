package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.screens.AuthScreen
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen
import com.example.mapsapp.ui.screens.PermissionsScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper(logOut: Boolean = false) {
    val navController = rememberNavController()
    NavHost(navController, Destinations.PantallaPermisos) {

        composable<Destinations.PantallaPermisos>{
            PermissionsScreen { navController.navigate(Destinations.PantallaAutenticacio) }
        }

        composable<Destinations.PantallaAutenticacio>{
            AuthScreen(logOut) { navController.navigate(Destinations.PantallaDrawer) }
        }

        composable<Destinations.PantallaDrawer>{
            DrawerScreen()
        }
    }
}