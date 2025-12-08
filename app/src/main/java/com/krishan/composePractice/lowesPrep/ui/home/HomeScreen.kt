package com.krishan.composePractice.lowesPrep.ui.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.krishan.composePractice.lowesPrep.ui.common.ShimmerItem
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state = viewModel.homeStateFlow.collectAsState().value
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.LightGray)
                .fillMaxSize()
        ) {
            when {
                state.userUiState.loading -> {
                    ShimmerItem()
                }

                state.userUiState.users.isNotEmpty() -> {
                    // user name and email section
                    Card(
                        shape = RoundedCornerShape(8.dp), modifier = Modifier
                            .padding(12.dp)
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Column {
                            state.userUiState.users.take(3).forEach {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                ) {
                                    // For rounded initial names
                                    Box(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(getColorFromName(it.name)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val initials = it.name
                                            .trim() // trims any leading or trailing commas
                                            .split(" ") // splits into list of words ["Sharon" "Long"]
                                            .filter { word -> word.isNotEmpty() } // gets rid of empty string (for edge cases like "  Jean   Claude Van Damme  "
                                            .take(2) // takes the first two words
                                            .mapNotNull { char ->
                                                char.firstOrNull()?.uppercase()
                                            } // takes the first char from these two words and turns them to uppercase
                                            .joinToString("") // makes them one string without any space
                                        Text(initials, style = MaterialTheme.typography.bodyLarge)
                                    }
                                    Column {
                                        Text(it.name, style = MaterialTheme.typography.bodyMedium)
                                        Text(it.email)
                                    }
                                }
                            }
                        }
                    }
                }

                state.userUiState.error?.isNotEmpty() == true -> {
                    Text("Something went wrong")
                }
            }


            when {
                state.photosUiState.loading -> {
                    ShimmerItem(modifier = Modifier.size(180.dp))
                }

                state.photosUiState.photos.isNotEmpty() -> {
                    Column(modifier = Modifier.size(600.dp)) {
                        Text("Photos", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.titleLarge)

                        val itemsList = state.photosUiState.photos

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(itemsList.take(6)) { item ->
                                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.aspectRatio(1f)) {
                                        AsyncImage(
                                            model = item.thumbnailUrl,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                state.photosUiState.error?.isNotEmpty() == true -> {
                    Text("Something went wrong")
                }
            }
            when {
                state.todosUiState.loading -> {
                    ShimmerLoadingItem()
                }

                state.todosUiState.todos.isNotEmpty() -> {
                    Column() {
                        Text("todos is successful")

                    }
                }

                state.todosUiState.error?.isNotEmpty() == true -> {
                    Text("Something went wrong")
                }
            }
        }
    }
}


fun getColorFromName(name: String): Color {
    val colors = listOf(
        Color(0xFFB3E5FC), // Light Blue
        Color(0xFFFFF9C4), // Light Yellow
        Color(0xFFC8E6C9), // Light Green
        Color(0xFFFFCCBC), // Light Orange
        Color(0xFFD1C4E9), // Light Purple
        Color(0xFFFFCDD2)  // Light Red
    )

    val index = (name.hashCode().absoluteValue) % colors.size
    return colors[index]
}

@Composable
fun LoadingContentScreen(isLoading: Boolean, data: List<Any>) {
    LazyColumn {
        items(count = if (isLoading) 1 else data.size) { index ->
            if (isLoading) {
                // Show Shimmer Placeholder
                ShimmerLoadingItem()
            } else {
                // Show Actual Content
                // ContentItem(data[index])
            }
        }
    }
}

@Composable
fun ShimmerLoadingItem() {
    Row(modifier = Modifier.padding(16.dp)) {
        // Image Placeholder (Circle shape)
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(MaterialTheme.shapes.small) // Use your desired shape
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            // Text Line 1 Placeholder (Rect shape)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Text Line 2 Placeholder (Shorter Rect shape)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .shimmerEffect()
            )
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "ShimmerInfiniteTransition")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // Distance the gradient travels
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // Speed of the shimmer
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse // Sweep back and forth
        ),
        label = "ShimmerTranslateAnimation"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation, y = translateAnimation),
        end = Offset(x = translateAnimation + 1000f, y = translateAnimation + 1000f)
    )

    // Apply the animated brush as a background
    this.background(brush = brush)
}

