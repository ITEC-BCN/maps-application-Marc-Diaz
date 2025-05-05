package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient


class MyApp: Application() {
    companion object {
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        database = MySupabaseClient(
            supabaseUrl = "https://gwcqsozqzdlxjrpijrbk.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd3Y3Fzb3pxemRseGpycGlqcmJrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU4MjgwMDMsImV4cCI6MjA2MTQwNDAwM30.DRH9jMeUskljY7JKo_6FSK9dKEjqZ5oglkmRfae8NHA"
        )
    }
}
