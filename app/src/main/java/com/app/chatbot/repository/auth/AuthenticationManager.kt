package com.app.chatbot.repository.auth

import android.content.Context
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.app.chatbot.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class AuthenticationManager(val context: Context) {

    private val auth = Firebase.auth

    fun createUserWithEmailAndPassword(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(task.exception?.message ?: "Unknown error"))
                }

                }
        awaitClose()
    }
    fun loginInWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(task.exception?.message ?: "Unknown error"))
                }
            }
        awaitClose()

    }

    fun forgetPassword(email: String): Flow<AuthResponse> = callbackFlow {
        auth.sendPasswordResetEmail(email)

    }
 }


