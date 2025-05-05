package com.example.mapsapp.data
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
class MySupabaseClient() {
    lateinit var marcador: SupabaseClient
    constructor(supabaseUrl: String, supabaseKey: String): this(){
        marcador = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
        }
    }
    suspend fun getAllMarcadores(): List<Marcador>{
        return marcador.from("Marcadores").select().decodeList()
    }

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

    suspend fun updateMarcador(id: Int, titulo: String, descripcion: String, imagen: String){
        this.marcador.from("Marcadores").update({
            set("titulo", titulo)
            set("descripcion", descripcion)
            set("imagen", imagen)
        }) { filter { eq("id", id) }}
    }

    suspend fun deleteMarcador(id: Int){
        marcador.from("Marcadores").delete{
            filter { eq("id", id) }
        }
    }
}