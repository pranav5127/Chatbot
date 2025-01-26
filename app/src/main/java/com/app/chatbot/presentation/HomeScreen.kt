package com.app.chatbot.presentation

import ChatHistoryViewModel
import ChatHistoryViewModelFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.chatbot.Screen
import com.app.chatbot.presentation.auth.signin.clearSession
import com.app.chatbot.presentation.components.ResponseCard
import com.app.chatbot.repository.auth.GoogleAuthClient
import com.app.chatbot.repository.db.ChatDatabase
import com.app.chatbot.repository.db.ChatMessage
import com.app.chatbot.repository.db.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    googleAuthClient: GoogleAuthClient,
    chatRepository: ChatRepository = ChatRepository(ChatDatabase.getDatabase(navController.context).chatHistoryDao())
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val chatHistoryViewModel: ChatHistoryViewModel = viewModel(
        factory = ChatHistoryViewModelFactory(chatRepository)
    )

    // Load chat history when the screen is created
    LaunchedEffect(Unit) {
        chatHistoryViewModel.loadChatHistory()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                drawerState = drawerState,
                scope = scope,
                chatHistoryViewModel = chatHistoryViewModel,
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
                TopBar(
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            bottomBar = {
                BottomBar()
            }
        ) { paddingValues ->
            Chats(paddingValues = paddingValues, chatHistoryViewModel = chatHistoryViewModel)
        }
    }
}

@Composable
fun AppDrawerContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    chatHistoryViewModel: ChatHistoryViewModel,
    onLogoutClick: () -> Unit
) {
    val chatHistory by chatHistoryViewModel.chatHistory.collectAsState()

    ModalDrawerSheet {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("History", modifier = Modifier.padding(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(chatHistory) { message ->
                    Text(
                        text = message.message,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onNavigationIconClick: () -> Unit,
    chatScreenViewModel: ChatScreenViewModel = viewModel(),
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
            IconButton(onClick = {
                coroutineScope.launch {
                    chatScreenViewModel.clearResponses()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.AddCircleOutline,
                    contentDescription = "New Chat",
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
fun Chats(
    chatScreenViewModel: ChatScreenViewModel = viewModel(),
    chatHistoryViewModel: ChatHistoryViewModel,
    paddingValues: PaddingValues
) {
    val responses = chatScreenViewModel.responses
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

                // Combine query and response as a single string and save it
                LaunchedEffect(response) {
                    val combinedMessage = "Query: ${response.query}\nResponse: ${response.response}"
                    chatHistoryViewModel.saveMessage(
                        ChatMessage(
                            message = combinedMessage,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }
}
