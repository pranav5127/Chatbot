package com.app.chatbot.presentation.auth.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.chatbot.Screen
import com.app.chatbot.repository.auth.AuthenticationManager

@Composable
fun ForgetPassword(
    navController: NavController
){
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context) }


 Surface {
     Column(
         modifier = Modifier.fillMaxSize(),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
         TextField(
             value = email,
             onValueChange = { email = it },
             label = { Text("Email") },
             placeholder = { Text("Enter your email") },
             singleLine = true,
             modifier = Modifier.fillMaxWidth()
         )
         Button(
             onClick = {
                 authenticationManager.forgetPassword(email)
                 navController.navigate(Screen.SignInScreen.route)
             },
             modifier = Modifier.fillMaxWidth()

         ){
             Text(
                 text = "ResetPassword"
             )
         }
     }
 }


}
