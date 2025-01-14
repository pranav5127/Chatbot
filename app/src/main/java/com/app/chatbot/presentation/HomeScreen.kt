package com.app.chatbot.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.chatbot.presentation.components.ResponseCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()
   

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                drawerState = drawerState,
                scope = scope,
                onDrawerItemClick = { /* Handle item click */ }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(onNavigationIconClick = {
                    scope.launch {
                        drawerState.open()
                    }
                })
            },
            bottomBar = {
                BottomBar()
            }

        ) { paddingValues ->
            Chats(paddingValues = paddingValues)

        }
    }
}

@Composable
fun AppDrawerContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    onDrawerItemClick: () -> Unit
) {
    ModalDrawerSheet {
        Text("History", modifier = Modifier.padding(16.dp))
        NavigationDrawerItem(
            label = { Text("Item 1") },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                onDrawerItemClick()
            }
        )
        NavigationDrawerItem(
            label = { Text("Item 2") },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                onDrawerItemClick()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onNavigationIconClick: () -> Unit) {

    TopAppBar(
        title = { Text(text = "Chatbot") },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Segment,
                    contentDescription = "List",
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.AddCircleOutline,
                    contentDescription = "New Chat",
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Options",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
fun BottomBar(chatScreenViewModel: ChatScreenViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 8.dp, vertical = 8.dp)

    ) {
        OutlinedTextField(

            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.ime),
            shape = RoundedCornerShape(24.dp),
            placeholder = { Text("Type a message...") },

            trailingIcon = {
                IconButton(
                    onClick = {
                        if (query.isNotEmpty()) {
                            chatScreenViewModel.sendQuery(query)
                            query = ""
                            keyboardController?.hide()
                        }
                    },
                    enabled = query.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send"
                    )
                }
            }
        )
    }
}


@Composable
fun Chats(chatScreenViewModel: ChatScreenViewModel = viewModel(), paddingValues: PaddingValues) {
    val responses = chatScreenViewModel.responses
    val isLoading = chatScreenViewModel.isLoading.value
    val errorMessage = chatScreenViewModel.errorMessage.value
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(responses.value) {
        if(responses.value.isNotEmpty()){
            coroutineScope.launch {
                lazyListState.animateScrollToItem(responses.value.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)

    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            state = lazyListState
        ) {
            items(responses.value) { response ->
                ResponseCard(response)
            }

        }
        // Loading and error indicators
        if (isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
