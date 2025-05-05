package com.example.mapsapp.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marcador
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

class MapViewModel(): ViewModel() {
    val db = MyApp.database

    //Marcadores
    private var _marcadores = MutableLiveData<List<Marcador>>()
    val marcadores = _marcadores
    private var _marcador = MutableLiveData<Marcador>()
    val marcador = _marcador

    //Obtener marcador
    fun getAllMarcadores() {
        CoroutineScope(Dispatchers.IO).launch {
            val dbMarcadores = db.getAllMarcadores()
            withContext(Dispatchers.Main) {
                _marcadores.value = dbMarcadores
            }
        }
    }

    //Añadir marcador
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertarMarcador(latLng: String) {
        val stream = ByteArrayOutputStream()
        _imagenBitMap.value?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        CoroutineScope(Dispatchers.IO).launch {
            var imageName = ""
            if (_imagenBitMap.value != null) {
                imageName = db.uploadImage(stream.toByteArray())
            }

            db.insertMarcador(Marcador(
                titulo = _titulo.value ?: "",
                descripcion = descripcion.value ?: "",
                imagen = imageName,
                latLng = latLng
            ))
        }
    }

    //Actualizar marcador
    fun updateMarcador(id: Int, titulo: String, descripcion: String, imagen : Bitmap?){
        val stream = ByteArrayOutputStream()
        imagen?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val imageName = _marcador.value?.imagen?.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        Log.d("PASA POR AQUI", "$imageName")
        CoroutineScope(Dispatchers.IO).launch {
            db.updateMarcador(
                id, titulo, descripcion,
                imageName = imageName.toString(),
                imageFile = stream.toByteArray()
            )
        }
    }

    //Borrar marcador
    fun deleteMarcador(id: Int, imagen : String){
        CoroutineScope(Dispatchers.IO).launch {
            db.deleteMarcador(id)
            db.deleteImage(imagen)
        }
    }

    //Obtener marcador
    fun getMarcadorById(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val dbMarcador = db.getMarcador(id)
            withContext(Dispatchers.Main) {
                _marcador.value = dbMarcador
                _titulo.value = dbMarcador.titulo
                _descripcion.value = dbMarcador.descripcion
            }
        }
    }

    //Datos Marcador
    private var _titulo = MutableLiveData<String>()
    val titulo = _titulo
    fun setTitulo(newTitulo: String){
        _titulo.value = newTitulo
    }

    private var _descripcion = MutableLiveData<String>()
    val descripcion = _descripcion
    fun setDescripcion(newDescripcion: String){
        _descripcion.value = newDescripcion
    }

    //Imagen
    private var _imagenURI = MutableLiveData<Uri>()
    val imagenURI = _imagenURI
    fun setImagenURI(new: Uri){
        _imagenURI.value = new
    }

    private var _imagenBitMap = MutableLiveData<Bitmap>()
    val imagenBitMap = _imagenBitMap
    fun setImagenBitMap(new: Bitmap){
        _imagenBitMap.value = new
    }

    fun createImageUri(context: Context) {
        val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        _imagenURI.value = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }


}