package com.app.chatbot.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.chatbot.Screen
import com.app.chatbot.presentation.auth.signin.clearSession
import com.app.chatbot.presentation.components.ResponseCard
import com.app.chatbot.repository.auth.GoogleAuthClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    googleAuthClient: GoogleAuthClient
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                drawerState = drawerState,
                scope = scope,
                onLogoutClick = {
                    scope.launch {
                        googleAuthClient.signOut()
                        navController.navigate(Screen.SignInScreen.route)
                        clearSession(navController.context.getSharedPreferences("user_prefs", 0))
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(onNavigationIconClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }, googleAuthClient = googleAuthClient)
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
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet {
        Text("History", modifier = Modifier.padding(16.dp))
        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                onLogoutClick()
            }
        )
        NavigationDrawerItem(
            label = { Text("Item 2") },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onNavigationIconClick: () -> Unit,
    googleAuthClient: GoogleAuthClient
) {
    val coroutineScope = rememberCoroutineScope()

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
            IconButton(onClick = {
                coroutineScope.launch {
                    googleAuthClient.signOut()
                }
            }) {
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
        if (responses.value.isNotEmpty()) {
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
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            state = lazyListState
        ) {
            items(responses.value) { response ->
                ResponseCard(response)
            }
        }

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
