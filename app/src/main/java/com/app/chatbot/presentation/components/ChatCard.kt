package com.app.chatbot.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuickChatCard(query: String){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.07f)
            .padding(8.dp)
            .clickable(onClick = {}),

    ){
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = query,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QuickChatCardPreview(){
    QuickChatCard(query = "Hello World")
}
