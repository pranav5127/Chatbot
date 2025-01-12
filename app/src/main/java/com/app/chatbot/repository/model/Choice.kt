package com.app.chatbot.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val index: Int,
    val message: Message
)
