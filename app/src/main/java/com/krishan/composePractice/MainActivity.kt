package com.krishan.composePractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.krishan.composePractice.ui.nav.Screen
import com.krishan.composePractice.ui.nav.User
import com.krishan.composePractice.ui.theme.ComposePracticeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

            ComposePracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text("Compose Practice") }) }) { innerPadding ->
//                    Column(modifier = Modifier.padding(innerPadding)) {
//                        DropdownLoadingBar()
//                    }

                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when (key) {
                                is Screen.Home -> NavEntry(key) {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .background(color = Color.DarkGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Button(onClick = {
                                            backStack.add(
                                                Screen.Details(
                                                    User(
                                                        id = "User1234",
                                                        name = "Mr. Peace",
                                                        email = "peace@gmail.com"
                                                    )
                                                )
                                            )
                                        }) {
                                            Text("navigate to Details")
                                        }
                                    }
                                }

                                is Screen.Details -> NavEntry(key) {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .background(color = Color.Gray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Button(onClick = {
                                            backStack.removeLastOrNull()
                                        }) {
                                            Text("User name: ${key.user.name} Navigate back to Home")
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposePracticeTheme {
        Greeting("Android")
    }
}