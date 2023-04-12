package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.presentation.ui.theme.LocalElevation
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Composable
fun DiaryHolder(diary: Diary, onClickDiary: (objectId: String) -> Unit) {

    val density = LocalDensity.current
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

        //vertical line
        Surface(
                modifier = Modifier
                        .width(spacing.spaceDoubleDp)
                        .height(componentHeight + spacing.spaceMedium),
                tonalElevation = elevation.level1
        ) {

        }

        Spacer(modifier = Modifier.width(spacing.spaceMedium))

        //Diary Entry

        Surface(
                modifier = Modifier
                        .clip(shape = Shapes().medium)
                        .onGloballyPositioned {
                            componentHeight = with(density) { it.size.height.toDp() }
                        }, tonalElevation = elevation.level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                DiaryHeader(moodName = diary.mood, time = )
            }
        }
    }

}