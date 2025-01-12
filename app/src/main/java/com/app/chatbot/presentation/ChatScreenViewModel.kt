package com.app.chatbot.presentation

import com.app.chatbot.repository.ChatRepository
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log

class ChatScreenViewModel : ViewModel() {
    private val repository = ChatRepository()

    private val _responses = mutableStateOf<List<String>>(emptyList())
    val responses: State<List<String>> = _responses

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun sendQuery(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result: String? = repository.sendMessage(query)
                if (result != null) {
                    _responses.value = listOf(result)
                } else {
                    _errorMessage.value = "Failed to get a response from the server."
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
                Log.e("ChatScreenViewModel", "Error sending query: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
