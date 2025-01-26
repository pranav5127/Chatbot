package com.app.chatbot.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.chatbot.repository.db.ChatMessage

@Dao
interface ChatHistoryDao {
    @Insert
    suspend fun insertMessage(message: ChatMessage)

    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC")
    suspend fun getChatHistory(): List<ChatMessage>

    @Query("DELETE FROM chat_history")
    suspend fun clearChatHistory()
}
