package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mapsapp.ui.navigation.MainNavigationWrapper
import com.example.mapsapp.ui.navigation.NavigationWrapper
import com.example.mapsapp.ui.theme.MapsAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
                MainNavigationWrapper()
            }
        }
    }
}



