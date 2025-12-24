package com.krishan.composePractice.pricelineprep.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.krishan.composePractice.pricelineprep.ui.home.PricelineHomeScreen

@Composable
fun PricelineNavigationHelper() {
    val backStack = remember { mutableStateListOf(Screen.Home) }
    NavDisplay(backStack = backStack, onBack = { backStack.removeLastOrNull() }, entryProvider = { key ->
        when (key) {
            is Screen.Home -> NavEntry(key) {
                PricelineHomeScreen()
            }
        }
    })
}

sealed class Screen {
    data object Home : Screen()
}