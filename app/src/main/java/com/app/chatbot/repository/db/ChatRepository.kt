package com.app.chatbot.repository.db

class ChatRepository(private val chatHistoryDao: ChatHistoryDao) {
    suspend fun saveMessage(message: ChatMessage) = chatHistoryDao.insertMessage(message)
    suspend fun getMessages() = chatHistoryDao.getChatHistory()
    suspend fun clearHistory() = chatHistoryDao.clearChatHistory()
}
