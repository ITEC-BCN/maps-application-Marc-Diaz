package com.example.mapsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mapsapp.data.SharedPreferencesHelper

class AuthViewModelFactory(private val shredPreferences: SharedPreferencesHelper, private val logOut: Boolean = false): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(shredPreferences, logOut) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
