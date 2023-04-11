package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.presentation.ui.theme.LocalElevation
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Composable
fun DiaryHolder(diary: Diary, onClickDiary: (objectId: String) -> Unit) {

    val spacing = LocalSpacing.current
    val elevation = LocalElevation.current

    //we will calculate the height of the side line from the component height

    var componentHeight by remember { mutableStateOf(spacing.spaceDefault) }


    Row(modifier = Modifier
            .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
            )
            {
                onClickDiary(diary._id.toString())
            })
    {

        Spacer(modifier = Modifier.width(spacing.spaceMedium))
        Surface(
                modifier = Modifier
                        .width(spacing.spaceDoubleDp)
                        .height(componentHeight),
                tonalElevation = elevation.level1
        ) {

        }
        Spacer(modifier = Modifier.width(spacing.spaceMedium))
    }

}