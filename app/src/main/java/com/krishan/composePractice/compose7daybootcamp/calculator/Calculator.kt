package com.krishan.composePractice.compose7daybootcamp.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calculator() {
    Scaffold(topBar = { TopAppBar(title = { Text("Calculator app") }) }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            val num1 = remember { mutableIntStateOf(0) }
            val num2 = remember { mutableIntStateOf(0) }
            val result = remember { mutableIntStateOf(0) }

            val calculatorText = remember {
                mutableStateOf("")
            }

            TextField(
                //state = rememberTextFieldState("0"),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                value = calculatorText.value,
                onValueChange = { newText ->
                    calculatorText.value = newText
                },
                textStyle = TextStyle(textAlign = TextAlign.Right))

            Text(
                result.intValue.toString(),
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .align(Alignment.End),
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.DarkGray)
            )

            LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                items(list) { calculatorItem ->
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(80.dp)
                            .background(shape = CircleShape, color = Color.DarkGray)
                            .clickable(onClick = {
                                calculatorText.value = calculatorItem.toString()
                                if (calculatorItem == "=") {
                                    calculate(num1.intValue, num2.intValue, calculatorItem.toString())
                                }
                            }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            calculatorItem.toString(),
                            style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CalculatorPreview() {
    Calculator()
}

val list: List<Any> = listOf("AC", "%", "<=", "/", 7, 8, 9, "*", 4, 5, 6, "-", 1, 2, 3, "+", "00", 0, ".", "=")


enum class CalculatorActions(val value: String) {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    MODULO("%"),
    EQUALS("="),
    ALL_CLEAR("AC"),
    BACK_SPACE("<=")
}

fun calculate(num1: Int, num2: Int, action: String) {
    when (action) {
        CalculatorActions.ADDITION.value -> num1 + num2
        CalculatorActions.SUBTRACTION.value -> num1 - num2
        CalculatorActions.MULTIPLICATION.value -> num1 * num2
        CalculatorActions.DIVISION.value -> divide(num1, num2)
        CalculatorActions.MODULO.value -> TODO()
        CalculatorActions.EQUALS.value -> TODO()
        CalculatorActions.ALL_CLEAR.value -> TODO()
        CalculatorActions.BACK_SPACE.value -> TODO()
    }
}

fun divide(num1: Int, num2: Int): Int = if (num2 != 0) num1 / num2 else -1
