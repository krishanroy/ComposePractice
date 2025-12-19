package com.krishan.composePractice.lowesPrep.ui.errorfix

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.unit.dp

@Composable
fun GroceryListScreen() {
    // MISTAKE 1: This list is not an observable state. Adding items won't update UI.
    val groceries = remember { mutableStateListOf("Milk", "Eggs", "Bread") }

    // MISTAKE 2: This state resets on every recomposition (e.g., when keyboard opens).
    val newItemText = remember { mutableStateOf("") }

    Column(modifier = Modifier.wrapContentHeight()) {
        TextField(
            value = newItemText.value,
            onValueChange = { newItemText.value = it }
        )

        Button(onClick = {
            if (newItemText.value.isNotBlank()) {
                groceries.add(newItemText.value)
            }
            newItemText.value = "" // clears text field
            // Even if this worked, we aren't clearing the text field!
        }) {
            Text("Add Item")
        }

        LazyColumn {
            // MISTAKE 3: No unique key provided. Compose will have trouble verifying
            // which items changed, leading to performance hits on large lists.
            items(groceries, key = {item -> item}) { item ->
                Text(item, modifier = Modifier)
            }
        }
    }
}