package com.app.chatbot.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.markdown.MarkdownParseOptions
import com.halilibo.richtext.ui.RichText


@Composable
fun UserQueryCard(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.8f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ResponseCard(text: String){
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 0.dp, end = 32.dp, top = 8.dp, bottom = 2.dp),

        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            MarkdownText(text)
        }
    }
}

@Composable
fun MarkdownText(text: String){
    val markdownParseOptions = MarkdownParseOptions(
        autolink = false
    )
    RichText(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),



    ){
        Markdown(
            text,
            markdownParseOptions = markdownParseOptions
            )
    }
}


@Preview
@Composable
fun ResponseCardPreview(){
    ResponseCard(text = "Hello World")

}