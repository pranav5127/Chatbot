import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.chatbot.repository.db.ChatMessage
import com.app.chatbot.repository.db.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatHistoryViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    // Load all saved chat history from the database
    fun loadChatHistory() {
        viewModelScope.launch {
            _chatHistory.value = repository.getMessages()
        }
    }

    // Save a single message to the database
    fun saveMessage(message: ChatMessage) {
        viewModelScope.launch {
            repository.saveMessage(message)
            loadChatHistory() // Update the current state
        }
    }

    // Save a list of messages as a batch (e.g., on "New Chat" click)
    fun saveMessages(messages: List<ChatMessage>) {
        viewModelScope.launch {
            messages.forEach { repository.saveMessage(it) }
            loadChatHistory()
        }
    }

    // Clear the history from the database
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _chatHistory.value = emptyList()
        }
    }
}



class ChatHistoryViewModelFactory(private val repository: ChatRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatHistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
