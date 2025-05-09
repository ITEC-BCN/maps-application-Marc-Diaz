package com.example.mapsapp.data
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mapsapp.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import java.time.format.DateTimeFormatter


class MySupabaseClient {
    lateinit var marcador: SupabaseClient
    lateinit var storage: io.github.jan.supabase.storage.Storage
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY

    constructor(){
        marcador = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
            install(io.github.jan.supabase.storage.Storage)
        }
        storage = marcador.storage
    }

    //Imagenes
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = java.time.LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imageName = storage.from("images").upload(path = "image_${fechaHoraActual.format(formato)}.png", data = imageFile)
        return buildImageUrl(imageFileName = imageName.path)
    }

    fun buildImageUrl(imageFileName: String) = "${this.supabaseUrl}/storage/v1/object/public/images/${imageFileName}"

    suspend fun deleteImage(imageName: String) {
        val imgName = imageName.removePrefix("https://gwcqsozqzdlxjrpijrbk.supabase.co/storage/v1/object/public/images/")
        marcador.storage.from("images").delete(imgName)
    }
    suspend fun getAllMarcadores(): List<Marcador>{
        return marcador.from("Marcadores").select().decodeList()
    }

    //Marcadores
    suspend fun getMarcador(id: Int): Marcador{
        return marcador.from("Marcadores").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marcador>()
    }

    suspend fun insertMarcador(marcador: Marcador){
        this.marcador.from("Marcadores").insert(marcador)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateMarcador(id: Int, titulo: String, descripcion: String, imageName: String, imageFile: ByteArray){
        deleteImage(imageName)
        val image = uploadImage(imageFile)

        Log.d("UPDATE path 1", image)
        this.marcador.from("Marcadores").update({
            set("titulo", titulo)
            set("descripcion", descripcion)
            set("imagen", image)
        })
        { filter { eq("id", id) }}
    }


    suspend fun deleteMarcador(id: Int){
        marcador.from("Marcadores").delete{
            filter { eq("id", id) }
        }
    }
}