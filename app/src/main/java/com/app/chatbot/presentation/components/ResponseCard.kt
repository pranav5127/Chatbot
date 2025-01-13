package com.app.chatbot.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.chatbot.presentation.model.ChatItem
import com.meetup.twain.MarkdownText

@Composable
fun UserQueryCard(text: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 4.dp),
        horizontalArrangement = Arrangement.End

        ){
    Card(
        modifier = Modifier
            .padding(8.dp),

        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun UserResponseCard(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(all = 4.dp),
        horizontalArrangement = Arrangement.Start
    ){
      Card(
          modifier = Modifier
              .padding(8.dp)
      ) {
            MarkdownParser(text = text)
        }
    }
}


@Composable
fun ResponseCard(userQueryResult: ChatItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        UserQueryCard(
            text = userQueryResult.query
        )

        UserResponseCard(
            text = userQueryResult.response
        )
    }
}


@Composable
fun MarkdownParser(text: String){
    val customTextStyle = TextStyle(
        lineHeight = 24.sp,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Normal,

    )
    MarkdownText(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
        markdown = text,
        style = LocalTextStyle.current.merge(customTextStyle)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResponseCardPreview(){
   Scaffold {paddingValues ->
       Spacer(modifier = Modifier.padding(paddingValues))
       ResponseCard(ChatItem("Hi", "Hello"))
   }
}