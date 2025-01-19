package com.app.chatbot.presentation.auth.signin

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.chatbot.R
import com.app.chatbot.Screen
import com.app.chatbot.repository.auth.AuthResponse
import com.app.chatbot.repository.auth.AuthenticationManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val PREFS_NAME = "user_prefs"
private const val KEY_EMAIL = "email"
private const val KEY_IS_LOGGED_IN = "is_logged_in"

@Composable
fun SignInScreen(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }
    val coroutineScope = rememberCoroutineScope()

    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Check if user is already logged in
    val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    if (isLoggedIn) {
        navController.navigate(Screen.HomeScreen.route) {
            popUpTo(Screen.SignInScreen.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                placeholder = { Text("Email") },
            )
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                placeholder = { Text("Password") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = showPassword,
                    onCheckedChange = { showPassword = it }
                )
                Text(text = "Show Password")
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Forgot Password?",
                    modifier = Modifier.clickable { /* Navigate to forgot password screen */ }
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = {
                    authenticationManager.loginInWithEmail(email, password)
                        .onEach { response ->
                            when (response) {
                                is AuthResponse.Success -> {
                                    saveSession(sharedPreferences, email)
                                    navController.navigate(Screen.HomeScreen.route) {
                                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
                                    }
                                }
                                is AuthResponse.Error -> Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        .launchIn(coroutineScope)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Don't have an account? ")
                Text(
                    text = "Sign Up",
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.SignUpScreen.route)
                    },
                    color = Color.Blue
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "or continue with")
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedButton(
                onClick = { /* Handle Google sign-in */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Sign In with Google")
            }
        }
    }
}

private fun saveSession(sharedPreferences: SharedPreferences, email: String) {
    sharedPreferences.edit()
        .putBoolean(KEY_IS_LOGGED_IN, true)
        .putString(KEY_EMAIL, email)
        .apply()
}

fun clearSession(sharedPreferences: SharedPreferences) {
    sharedPreferences.edit()
        .putBoolean(KEY_IS_LOGGED_IN, false)
        .remove(KEY_EMAIL)
        .apply()
}
