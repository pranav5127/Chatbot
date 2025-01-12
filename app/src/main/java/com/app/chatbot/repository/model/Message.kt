package com.app.chatbot.repository.model

import kotlinx.serialization.Serializable


@Serializable
data class Message(
    val role: String,
    val tool_calls: String? = null,
    val content: String
)