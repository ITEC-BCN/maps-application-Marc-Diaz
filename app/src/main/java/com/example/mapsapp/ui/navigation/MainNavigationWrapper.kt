package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.AuthScreen
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen
import com.example.mapsapp.ui.screens.PermissionsScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Destinations.PantallaPermisos) {

        composable<Destinations.PantallaPermisos>{
            PermissionsScreen { navController.navigate(Destinations.PantallaAutenticacio(false)) }
        }

        composable<Destinations.PantallaAutenticacio>{
                backStackEntry ->
            val pantallaAuth = backStackEntry.toRoute<Destinations.PantallaAutenticacio>()
            AuthScreen(pantallaAuth.logOut) { navController.navigate(Destinations.PantallaDrawer){
                popUpTo<Destinations.PantallaDrawer>{ inclusive = true}
            } }
        }

        composable<Destinations.PantallaDrawer>{
            DrawerScreen{
                navController.navigate(Destinations.PantallaAutenticacio(true)){
                    popUpTo<Destinations.PantallaAutenticacio>{ inclusive = true}
                }
            }
        }
    }
}