package com.krishan.composePractice.pricelineprep.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Restaurant
import androidx.compose.material.icons.sharp.Work
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricelineHomeScreen() {
    Scaffold(topBar = { TopAppBar(title = {Text("Book new flights")} ) }, bottomBar = {
        BottomAppBar {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Home, contentDescription = "")
                    Text("Home")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Work, contentDescription = "")
                    Text("Work")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Restaurant, contentDescription = "")
                    Text("Restaurant")
                }
            }
        }
    }) { paddingValues ->
        Text("Price line home screen", modifier = Modifier.padding(paddingValues).padding(12.dp))
    }
}

@Preview
@Composable
fun PricelineHomeScreenPreview() {
    PricelineHomeScreen()
}