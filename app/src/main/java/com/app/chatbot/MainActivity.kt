package com.app.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.app.chatbot.presentation.auth.signin.MainScreen
import com.app.chatbot.presentation.auth.signin.SignInScreen
import com.app.chatbot.presentation.auth.signup.SignUpScreen
import com.app.chatbot.ui.theme.ChatbotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            ChatbotTheme {
                MainNavigation()
            }
        }
    }
}

