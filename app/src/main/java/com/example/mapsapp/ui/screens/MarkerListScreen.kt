package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.data.Marcador
import com.example.mapsapp.viewmodels.MapViewModel

@Composable
fun MarkerListScreen(modifier: Modifier = Modifier, navigateToDetail: (Int) -> Unit) {
    val appViewModel: MapViewModel = viewModel<MapViewModel>()
    val marcadores by appViewModel.marcadores.observeAsState(emptyList())
    LaunchedEffect(marcadores) {
        appViewModel.getAllMarcadores()
    }
    Log.d("Lista Marcadores", "$marcadores")

    LazyColumn(modifier = modifier.fillMaxSize()){
        itemsIndexed(
            marcadores,
            key = { _, marcador -> marcador.id!! }
        ){ _, marcador ->
            MarcadorSwipe(marcador, navigateToDetail, appViewModel)
        }
    }

}

@Composable
fun MarcadorSwipe(
    marcador: Marcador,
    navigateToDetail: (Int) -> Unit,
    appViewModel : MapViewModel
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart){
                appViewModel.deleteMarcador(marcador.id!!, marcador.imagen)
                true
            }
            else { false }

        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color(0xFFFF1744)
                        else Color.Transparent
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete"
                )
            }
        },
        content = {
            MarcadorItem(marcador, navigateToDetail)
        },
        enableDismissFromStartToEnd = false
    )
}


@Composable
fun MarcadorItem(
    marcador: Marcador,
    navigateToDetail: (Int) -> Unit
) {
    ListItem(
        modifier = Modifier.clip(MaterialTheme.shapes.small).clickable{ navigateToDetail(marcador.id!!)},
        headlineContent = {
            Text(
                marcador.titulo,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                marcador.descripcion,
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingContent = {
            Icon(
                Icons.Filled.Place,
                contentDescription = "person icon",
                Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(10.dp)
            )
        },
    )
}