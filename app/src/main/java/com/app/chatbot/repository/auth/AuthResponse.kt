package com.app.chatbot.repository.auth

interface AuthResponse {

    data object Success: AuthResponse
    data class Error(val message: String): AuthResponse
    data object Loading: AuthResponse
}