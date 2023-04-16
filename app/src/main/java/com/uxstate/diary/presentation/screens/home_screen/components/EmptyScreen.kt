package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Composable
fun EmptyPage(title: String = "Empty Diary", subtitle: String = "Write Something  ") {
    val spacing = LocalSpacing.current
    Column(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing.spaceLarge),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
                text = title,
                style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Medium
                )
        )
        Text(
                text = subtitle,
                style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal
                )
        )
    }
}