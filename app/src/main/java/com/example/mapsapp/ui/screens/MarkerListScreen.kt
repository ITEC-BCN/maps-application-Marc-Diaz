package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Marcador
import com.example.mapsapp.viewmodels.MapViewModel

@Composable
fun MarkerListScreen(modifier: Modifier = Modifier, navigateToDetail: (Int) -> Unit) {
    val appViewModel: MapViewModel = viewModel<MapViewModel>()
    val marcadores by appViewModel.marcadores.observeAsState(mutableListOf())
    LaunchedEffect(marcadores) {
        appViewModel.getAllMarcadores()
    }
    Log.d("Lista Marcadores", "$marcadores")

    LazyColumn(modifier = modifier) {
        items(marcadores) { marcador ->
            val dissmissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        appViewModel.deleteMarcador(marcador.id!!, marcador.imagen)
                        appViewModel.getAllMarcadores()
                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismissBox(state = dissmissState, backgroundContent = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                        .padding(end = 7.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }) {
                MarcadorItem(marcador, navigateToDetail = navigateToDetail)
            }

        }
    }

}

@Composable
fun MarcadorItem(
    marcador: Marcador,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit
) {
    Card(
        border = BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(0.dp),
        modifier = modifier
            .height(50.dp),
        onClick = {
            navigateToDetail(marcador.id!!)
        }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Default.Place,
                contentDescription = ""
            )
            Text(
                text = marcador.titulo,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize()
            )
        }

    }
}