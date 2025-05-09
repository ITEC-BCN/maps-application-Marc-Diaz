package com.example.mapsapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.AuthResponse
import com.example.mapsapp.viewmodels.AuthViewModel


@Composable
fun AuthScreen(navigateToHome : () -> Unit){
    val context = LocalContext.current
    val authViewModel = viewModel<AuthViewModel>()
    val email by authViewModel.email.observeAsState("")
    val password by authViewModel.password.observeAsState("")
    val showError by authViewModel.showError.observeAsState(false)
    val authState by authViewModel.authState.observeAsState()

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { authViewModel.editEmail(it) },
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            label = { Text("Mail") }
        )
        PasswordTextField(password){
            authViewModel.editPassword(it)
        }
        Button(
            onClick = {
                authViewModel.signIn()
            },
            content = {
                Text("Sign In")
            }
        )
        Button(
            onClick = {
                authViewModel.signUp()
            },
            content = {
                Text("Sign Up")
            }
        )


        //Autentificacion
        if (showError) {
            val errorMessage = (authState as AuthResponse.Error).message
            if (errorMessage!!.contains("invalid_credentials")) {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "An error has ocurred", Toast.LENGTH_LONG).show()
            }
            authViewModel.errorMessageShowed()
        } else {
            if (authState != null && authState is AuthResponse.Success) {
                navigateToHome()
            }
        }
    }
}

@Composable
fun PasswordTextField(password: String, editPassword: (String) -> Unit) {
    val state = remember { TextFieldState(password) }
    var showPassword by remember { mutableStateOf(false) }

    // Sync external password to internal state
    LaunchedEffect(password) {
        if (state.text.toString() != password) {
            state.setTextAndSelectAll(password)
        }
    }

    // Sync internal state changes back to parent
    LaunchedEffect(state.text) {
        val current = state.text.toString()
        if (current != password) {
            editPassword(current)
        }
    }

    BasicSecureTextField(
        state = state,
        textObfuscationMode =
            if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .padding(6.dp),
        decorator = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                ) {
                    innerTextField()
                }
                Icon(
                    if (showPassword) {
                        Icons.Outlined.Lock
                    } else {
                        Icons.Filled.Lock
                    },
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .requiredSize(48.dp).padding(16.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        }
    )
}
