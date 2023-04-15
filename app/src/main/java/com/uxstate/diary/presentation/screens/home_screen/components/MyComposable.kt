package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MyComposableFunction() {
    var counter by remember { mutableStateOf(0)



    }

    val doubledCounter by derivedStateOf {
        counter * 2
    }

    Button(onClick = { counter++ }) {
        Text("Click me")
    }

    Text("Counter: $counter")
    Text("Doubled counter: $doubledCounter")
}