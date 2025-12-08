package com.krishan.composePractice.lowesPrep.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.krishan.composePractice.lowesPrep.ui.home.HomeScreen
import com.krishan.composePractice.lowesPrep.ui.home.HomeViewModel

@Composable
fun NavigationHelper() {
    val backStack: SnapshotStateList<AllScreen> = remember { mutableStateListOf(AllScreen.Home) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is AllScreen.Home -> NavEntry(key) {
                    val viewModel: HomeViewModel = hiltViewModel()
                    HomeScreen(viewModel)
                }
            }
        })
}

sealed class AllScreen {
    data object Home : AllScreen()
}