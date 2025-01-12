package com.app.chatbot.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class MistralResponse(
    val choices: List<Choice>
)
