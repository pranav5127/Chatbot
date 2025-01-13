package com.app.chatbot.repository

import android.util.Log
import com.app.chatbot.BuildConfig
import com.app.chatbot.repository.model.MistralRequest
import com.app.chatbot.repository.model.MistralResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class ChatRepository {

    private val apiKey = BuildConfig.apiKeySafe

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 20000
            connectTimeoutMillis = 5000
            socketTimeoutMillis = 20000
        }
    }

    suspend fun sendMessage(message: String): String? {
        val requestPayload = MistralRequest(
            model = "mistral-large-latest",
            messages = listOf(mapOf("role" to "user", "content" to message))
        )

        try {
            val response = client.post("https://api.mistral.ai/v1/chat/completions") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $apiKey")
                }
                setBody(requestPayload)
            }


            Log.d("com.app.chatbot.repository.ChatRepository", "Response Status: ${response.status}")
            Log.d("com.app.chatbot.repository.ChatRepository", "Response Body: ${response.bodyAsText()}")

            if (response.status.value == 200) {
                val responseBody = response.body<MistralResponse>()
                val messageContent = responseBody.choices.firstOrNull()?.message?.content

                Log.d("com.app.chatbot.repository.ChatRepository", "Message Content: $messageContent")
                return messageContent
            } else {
                Log.e("com.app.chatbot.repository.ChatRepository", "Error: ${response.status}")
            }
        } catch (e: Exception) {
            Log.e("com.app.chatbot.repository.ChatRepository", "Request failed: ${e.message}")
        }
        return null
    }
}
