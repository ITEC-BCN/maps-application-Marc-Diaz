package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MapViewModel
import androidx.core.graphics.scale
import com.example.mapsapp.data.MySupabaseClient

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarkerScreen(latLng: String, navigateBack: () -> Unit) {
    val context = LocalContext.current
    val appViewModel: MapViewModel = viewModel<MapViewModel>()
    val titulo by appViewModel.titulo.observeAsState("")
    val descripcion by appViewModel.descripcion.observeAsState("")
    val imagenURI by appViewModel.imagenURI.observeAsState()
    val imagenBitmap by appViewModel.imagenBitMap.observeAsState()
    val cargados by appViewModel.cargados.observeAsState()

    //Navegacion
    if (cargados == true) {
        navigateBack()
    } else if (cargados == false) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    //Camara
    else {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && imagenURI != null) {
                    val stream = context.contentResolver.openInputStream(imagenURI!!)
                    stream?.use {
                        // Decodificar el flujo a un Bitmap
                        val originalBitmap = BitmapFactory.decodeStream(it)

                        // Obtener las dimensiones originales de la imagen
                        val originalWidth = originalBitmap.width
                        val originalHeight = originalBitmap.height

                        // Definir el aspect ratio (relación entre ancho y alto)
                        val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

                        // Establecer el tamaño máximo que deseas para la imagen (por ejemplo, un ancho máximo)
                        val maxWidth = 800 // Puedes establecer el valor que prefieras

                        // Calcular el nuevo ancho y alto manteniendo el aspect ratio
                        val newWidth = maxWidth
                        val newHeight = (newWidth / aspectRatio).toInt()

                        // Redimensionar el bitmap mientras se mantiene el aspect ratio
                        val resizedBitmap = originalBitmap.scale(newWidth, newHeight)

                        // Establecer el Bitmap redimensionado en el ViewModel
                        appViewModel.setImagenBitMap(resizedBitmap)
                    } ?: run {
                        Log.e("TakePicture", "Error al abrir InputStream para la URI de la imagen.")
                    }
                } else {
                    Log.e("TakePicture", "La imagen no fue tomada o la URI de la imagen es nula.")
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

        //Dialogo
        var showDialog by remember { mutableStateOf(false) }
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { appViewModel.setTitulo(it) },
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                label = { Text("Titulo") }
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { appViewModel.setDescripcion(it) },
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                label = { Text("Descripcion") }
            )
            Button(
                onClick = {
                    showDialog = true

                }
            ) {
                Text("Abrir camara o galeria")
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

            imagenBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

            }
            Button(
                onClick = {
                    appViewModel.insertarMarcador(latLng)
                }
            ) {
                Text("Add")
            }
        }
    }
}
