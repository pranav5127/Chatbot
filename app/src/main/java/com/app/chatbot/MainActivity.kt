package com.app.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.app.chatbot.repository.auth.GoogleAuthClient
import com.app.chatbot.ui.theme.ChatbotTheme

class  MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        val googleAuthClient = GoogleAuthClient(this)
        setContent {
            ChatbotTheme {
                MainNavigation(googleAuthClient)
            }
        }
    }
}