package com.example.mapsapp.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mapsapp.viewmodels.MapViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailMarkerScreen(marcadorId: Int, navigateBack: () -> Unit) {
    val context = LocalContext.current
    val appViewModel: MapViewModel = viewModel<MapViewModel>()
    val marcador by appViewModel.marcador.observeAsState()
    val titulo by appViewModel.titulo.observeAsState("")
    val descripcion by appViewModel.descripcion.observeAsState("")
    val imagenURI by appViewModel.imagenURI.observeAsState()
    val imagenBitmap by appViewModel.imagenBitMap.observeAsState()

    //Camara
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imagenURI != null) {
                val stream = context.contentResolver.openInputStream(imagenURI!!)
                appViewModel.setImagenBitMap(BitmapFactory.decodeStream(stream))
            }
        }

    //Galeria
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                appViewModel.setImagenURI(it)
                val stream = context.contentResolver.openInputStream(it)
                appViewModel.setImagenBitMap(BitmapFactory.decodeStream(stream))
            }
        }

    val imageLoader = ImageLoader(context)

    LaunchedEffect(Unit) {
        imageLoader.memoryCache?.clear()
        imageLoader.diskCache?.clear()
    }

    //Dialogo
    var showDialog by remember { mutableStateOf(false) }
    //Obtener marcador
    LaunchedEffect(marcador) {
        appViewModel.getMarcadorById(marcadorId)
    }
    Log.d("Imagen path", "${marcador?.imagen}")
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        marcador?.let {
            TextField(
                value = titulo,
                onValueChange = { appViewModel.setTitulo(it) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()

            )
            TextField(
                value = descripcion,
                onValueChange = { appViewModel.setDescripcion(it) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        Button(
            onClick = {
                showDialog = true
            },
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            ),
            shape = RectangleShape
        ) {
            if (imagenBitmap == null){
                Image(
                    rememberAsyncImagePainter(marcador?.imagen),
                    contentDescription = "",
                    modifier = Modifier.size(300.dp).clip(RoundedCornerShape(12.dp)),contentScale = ContentScale.Crop
                )
            }
            imagenBitmap?.let {
                Image(
                    it.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.size(300.dp).clip(RoundedCornerShape(12.dp)),contentScale = ContentScale.Crop
                )
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Selecciona una opción") },
                text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        appViewModel.createImageUri(context)
                        launcher.launch(imagenURI)
                    }) { Text("Tomar Foto") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        pickImageLauncher.launch("image/*")
                    }) { Text("Elegir de Galería") }
                }
            )
        }

        Log.d("MARCADOR", "$marcador")
        Log.d("MARCADOR map", "$imagenBitmap")
        Button(
            onClick = {
                appViewModel.updateMarcador(
                    marcadorId, titulo = titulo, descripcion = descripcion, imagenBitmap
                )
                navigateBack()
            }
        ) {
            Text("Guardar")
        }
    }
}