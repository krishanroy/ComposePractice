package com.krishan.composePractice.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay


private data object Check
private data object Edit
private data object Mic
private data object Image

// To prevent adding the same non null item again and again
fun SnapshotStateList<Any>.addIfNotThere(item: Any) {
    if (this.last() != item) {
        this.add(item)
    }
}

@Composable
fun BottomAppBarExample() {
    val backStack: SnapshotStateList<Any> = remember { mutableStateListOf<Any>(Check) }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { backStack.addIfNotThere(Check) }) {
                        Icon(Icons.Filled.Check, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { backStack.addIfNotThere(Edit) }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { backStack.addIfNotThere(Mic) }) {
                        Icon(
                            Icons.Filled.Mic,
                            contentDescription = "Localized description",
                        )
                    }
                    IconButton(onClick = { backStack.addIfNotThere(Image) }) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Localized description",
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { /* do something */ },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                }
            )
        },
    ) { innerPadding ->
        NavDisplay(backStack = backStack, onBack = { backStack.removeLastOrNull() }, entryProvider = entryProvider {
            entry<Check> {
                // check composable
                CheckComposable()
            }
            entry<Edit> {
                // Edit composable
                EditComposable()
            }
            entry<Mic> {
                // Mic composable
                MicComposable()
            }
            entry<Image> {
                // Image composable
                ImageComposable()
            }
        })
    }
}

@Composable
fun CheckComposable() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Check Bottom Nav bar")
    }
}

@Composable
fun EditComposable() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Edit Bottom Nav bar")
    }
}

@Composable
fun MicComposable() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Mic Bottom Nav bar")
    }
}

@Composable
fun ImageComposable() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Image Bottom Nav bar")
    }
}