package com.krishan.composePractice.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun DropdownLoadingBar() {
    var startAnimation by remember { mutableStateOf(IndicatorState.HIDDEN) }

    LaunchedEffect(Unit) {
        startAnimation = IndicatorState.DROPPING_DOWN

        delay(2000L)

        startAnimation = IndicatorState.GOING_UP
    }
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = startAnimation == IndicatorState.DROPPING_DOWN,
            enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(durationMillis = 1500)) + fadeIn(
                animationSpec = tween(durationMillis = 1_000)
            ),
            exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(durationMillis = 1500)) + fadeOut(
                animationSpec = tween(durationMillis = 1000)
            )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 100.dp)
                    .size(40.dp)
            )
        }
    }
}

enum class IndicatorState {
    HIDDEN,
    DROPPING_DOWN,
    GOING_UP
}