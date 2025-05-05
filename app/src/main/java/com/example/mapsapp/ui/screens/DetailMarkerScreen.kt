package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Marcador
import com.example.mapsapp.viewmodels.MapViewModel

@Composable
fun DetailMarkerScreen(marcadorId: Int, navigateBack: ()-> Unit){

    val appViewModel: MapViewModel = viewModel<MapViewModel>()
    val marcador by appViewModel.marcador.observeAsState()
    val titulo by appViewModel.titulo.observeAsState("")
    val descripcion by appViewModel.descripcion.observeAsState("")
    LaunchedEffect(marcador) {
        appViewModel.getMarcadorById(marcadorId)
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        marcador?.let {
            TextField(
                value = titulo,
                onValueChange = {appViewModel.setTitulo(it)},
                modifier = Modifier.padding(8.dp).fillMaxWidth()

            )
            TextField(
                value = descripcion,
                onValueChange = {appViewModel.setDescripcion(it)},
                modifier = Modifier.padding(8.dp).fillMaxWidth()
            )
        }

        Button(
            onClick = {
                appViewModel.updateMarcador(marcadorId, titulo = titulo, descripcion = descripcion, imagen = "")
                navigateBack()
            }
        ) {
            Text("Guardar")
        }
    }
}