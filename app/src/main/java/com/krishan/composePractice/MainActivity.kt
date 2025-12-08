package com.krishan.composePractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.krishan.composePractice.lowesPrep.ui.navigation.NavigationHelper
import com.krishan.composePractice.ui.theme.ComposePracticeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePracticeTheme {
                NavigationHelper()
            }
//            val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
//            var homeState by remember { mutableStateOf(HomeState.INITIAL) }
//            val scope = rememberCoroutineScope()
//
//            ComposePracticeTheme {
//                NavDisplay(
//                    backStack = backStack,
//                    onBack = { backStack.removeLastOrNull() },
//                    entryProvider = { key ->
//                        when (key) {
//                            is Screen.Home -> NavEntry(key) {
//                                Scaffold(
//                                    modifier = Modifier.fillMaxSize(),
//                                    topBar = { TopAppBar(title = { Text("Compose Practice") }) }) { innerPadding ->
//                                    Box(
//                                        Modifier
//                                            .padding(innerPadding)
//                                            .fillMaxSize()
//                                            .background(color = Color.DarkGray),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Button(modifier = Modifier.height(40.dp), onClick = {
//                                            scope.launch {
//                                                homeState = HomeState.LOADING
//                                                delay(2000)
////                                                backStack.add(
////                                                    Screen.Details(
////                                                        User(
////                                                            id = "User1234",
////                                                            name = "Mr. Peace",
////                                                            email = "peace@gmail.com"
////                                                        )
////                                                    )
////                                                )
//                                                backStack.add(Screen.BottomNav)
//                                                homeState = HomeState.LOADED
//                                            }
//                                        }) {
//                                            if (homeState == HomeState.LOADING) {
//                                                CircularProgressIndicator(
//                                                    modifier = Modifier.size(20.dp),
//                                                    color = Color.White,
//                                                    strokeWidth = 2.dp,
//                                                )
//                                            } else {
//                                                Text("navigate to Details")
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
////                            is Screen.Details -> NavEntry(key) {
////                                MkbhdPanelAppClickAnimation()
////                            }
//                            is Screen.BottomNav -> NavEntry(key) {
//                                //BottomAppBarExample()
//                                ktAcademyComposeModifierChallenge()
//                            }
//                        }
//                    }
//                )
//
//            }
        }
    }
}

enum class HomeState {
    INITIAL,
    LOADING,
    LOADED
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