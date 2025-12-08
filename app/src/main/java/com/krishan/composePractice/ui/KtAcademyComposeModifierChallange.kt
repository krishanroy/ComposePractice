package com.krishan.composePractice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.krishan.composePractice.R

@Composable
fun ktAcademyComposeModifierChallenge() {
    Scaffold() { innerPadding ->
//        Box(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(10.dp)
//                .size(100.dp)
//                //.background(Color.LightGray)
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.androidengineer),
//                contentDescription = null,
//                modifier = Modifier
////                    .padding(20.dp)
////                    .border(3.dp, Color.Red)
//                    //.offset(10.dp, (10).dp)
//                    .background(Color.Blue)
//            )
//        }
        Box(
            modifier = Modifier.padding(innerPadding).padding(10.dp), // container size
            contentAlignment = Alignment.BottomEnd // default alignment for children
        ) {
            Image(
                painter = painterResource(R.drawable.androidengineer),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // makes the image circular
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.Check, // or any icon
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.Gray, CircleShape)
                    .padding(4.dp)
            )
        }
    }
}