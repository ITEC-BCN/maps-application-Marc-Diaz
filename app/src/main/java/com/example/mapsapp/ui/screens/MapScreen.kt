package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.SupabaseManager
import com.example.mapsapp.utils.stringToLatLng
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.AuthViewModelFactory
import com.example.mapsapp.viewmodels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(navigateToCreateMarkerScreen : (LatLng) -> Unit, navigateToDetail: (Int) -> Unit){
    val context = LocalContext.current
    val appViewModel: MapViewModel = viewModel<MapViewModel>()
    val marcadores by appViewModel.marcadores.observeAsState(emptyList())

    val userLocation by appViewModel.userLocation.observeAsState()
    val cameraPosition = rememberCameraPositionState()

    LaunchedEffect(marcadores) {
        appViewModel.getAllMarcadores()
    }

    LaunchedEffect(userLocation) {
        appViewModel.updatetUserLocation(context)
        userLocation?.let {
            cameraPosition.position = CameraPosition.fromLatLngZoom(userLocation!!, 17f)
        }
    }

    Column(Modifier.fillMaxSize()) {

        GoogleMap(
            Modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = true),
            cameraPositionState = cameraPosition,
            onMapLongClick = {
                navigateToCreateMarkerScreen(it)
            }
        ){
            for (i in 0 until marcadores!!.size){
                Marker(
                    state = MarkerState(position = stringToLatLng(marcadores[i].latLng)),
                    title = marcadores[i].titulo,
                    snippet = marcadores[i].descripcion,
                    onClick = {
                        navigateToDetail(marcadores[i].id!!)
                        true
                    }

                )
            }
        }


    }
}