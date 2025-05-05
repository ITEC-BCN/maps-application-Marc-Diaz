package com.example.mapsapp.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marcador
import com.example.mapsapp.utils.latLangToString
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    fun insertarMarcador(latLng: LatLng){

        val marcador = Marcador(
            titulo = _titulo.value ?: "",
            descripcion = _descripcion.value ?: "",
            imagen = "",
            latLng = latLangToString(latLng)
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.insertMarcador(marcador)
        }
        Log.d("LISTA", "${marcadores.value}")
    }

    //Actualizar marcador
    fun updateMarcador(id: Int, titulo: String, descripcion: String, imagen : String){
        CoroutineScope(Dispatchers.IO).launch {
            db.updateMarcador(id, titulo, descripcion, imagen)
        }
    }

    //Borrar marcador
    fun deleteMarcador(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            db.deleteMarcador(id)
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