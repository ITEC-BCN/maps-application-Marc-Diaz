package com.example.mapsapp.data
import android.content.Context
import com.example.mapsapp.BuildConfig
import com.example.mapsapp.utils.AuthResponse
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient


class SupabaseManager(private val context: Context) {
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY
    private val supabase = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        install(Auth) {
            autoLoadFromStorage = true
        }
    }
    suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthResponse {
        try {
            val result = supabase.auth.signUpWith(Email){
                email = emailValue
                password = passwordValue

            }
            result?.let {
                SessionManager.saveSession(context, emailValue, passwordValue)
            }
            return AuthResponse.Success
        } catch (e: Exception) {
            return AuthResponse.Error(e.localizedMessage)
        }
    }

    suspend fun signInWithEmail(emailValue: String, passwordValue: String): AuthResponse {
        try {
            val result = supabase.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            result.let {
                SessionManager.saveSession(context, emailValue, passwordValue)
            }
            return AuthResponse.Success
        } catch (e: Exception) {
            return AuthResponse.Error(e.localizedMessage)
        }
    }
    suspend fun restoreSession(): AuthResponse {
        // Intentar recuperar la sesión guardada
        val (emailValue, passwordValue) = SessionManager.getSession(context)
        if (!emailValue.isNullOrEmpty() && !passwordValue.isNullOrEmpty()) {
            // Restaurar la sesión de Supabase si los tokens están presentes
            supabase.auth.signInWith(Email){
                email = emailValue
                password = passwordValue
            }
            return AuthResponse.Success
        }
        return AuthResponse.Error("")
    }

    suspend fun signOut() {
        // Limpiar sesión y eliminar tokens
        supabase.auth.signOut()
        SessionManager.clearSession(context)
    }
    suspend fun getSession(): Pair<String?, String?> {
        return SessionManager.getSession(context)
    }
}