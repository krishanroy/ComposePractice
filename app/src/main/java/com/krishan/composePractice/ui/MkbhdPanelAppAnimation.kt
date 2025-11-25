package com.krishan.composePractice.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krishan.composePractice.R
import com.krishan.composePractice.ui.theme.ComposePracticeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MkbhdPanelAppClickAnimation() {
    var isSelected by remember { mutableStateOf(false) }
    val scaleAnimation = animateFloatAsState(targetValue = if (isSelected) 1f else 0f, animationSpec = tween(1200))


    Scaffold(topBar = { TopAppBar(title = { Text("Cool click Animation") }) }) { innerPadding ->
        Card(
            modifier = Modifier
                .padding(12.dp)
                .size(400.dp)
                .padding(innerPadding)
                .clickable {
                    isSelected = !isSelected
                }
        ) {
            Box() {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(R.drawable.androidengineer),
                        contentDescription = "logo",
                        modifier = Modifier
                            .clip(CircleShape)
                    )

                    Text("Click anywhere to see cool animation")
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    scale(scaleAnimation.value) {
                        drawCircle(
                            color = Color(0xD7FF5722), radius = 400.dp.toPx()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MkbhdPanelAppClickAnimationPreview() {
    ComposePracticeTheme() {
        MkbhdPanelAppClickAnimation()
    }
}