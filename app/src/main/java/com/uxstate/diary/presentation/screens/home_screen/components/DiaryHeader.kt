package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.uxstate.diary.R
import com.uxstate.diary.domain.model.Mood
import com.uxstate.diary.presentation.ui.theme.LocalSpacing
import java.time.Instant

@Composable
fun DiaryHeader(moodName: String, time: Instant) {

    val spacing = LocalSpacing.current

    //value of returns the actual enum
    val mood by remember { mutableStateOf(Mood.valueOf(moodName)) }

    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .background(mood.containerColor)
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                    modifier = Modifier.size(spacing.spaceMedium),
                    painter = painterResource(id = mood.icon),
                    contentDescription = stringResource(R.string.mood_icon)
            )

            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            Text(
                    text = Mood.valueOf(moodName).name,
                    style = TextStyle(
                            color = mood.contentColor,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
            )
        }
    }
}