package com.app.chatbot

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.chatbot.presentation.HomeScreen
import com.app.chatbot.presentation.auth.signin.SignInScreen
import com.app.chatbot.presentation.auth.signup.SignUpScreen
import com.app.chatbot.repository.auth.GoogleAuthClient

@Composable
fun MainNavigation(googleAuthClient: GoogleAuthClient){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SignInScreen.route){
        composable(route = Screen.SignInScreen.route){
            SignInScreen(navController = navController, googleAuthClient = googleAuthClient)
        }
        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController = navController, googleAuthClient = googleAuthClient)
        }

    }
}


