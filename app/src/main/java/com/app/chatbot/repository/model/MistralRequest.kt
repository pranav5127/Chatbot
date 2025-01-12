package com.app.chatbot.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class MistralRequest(
    val model: String,
    val messages: List<Map<String, String>>
)