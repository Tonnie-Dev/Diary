package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Composable
fun DiaryHolder(diary: Diary, onClickDiary: (objectId: String) -> Unit) {

    val spacing = LocalSpacing.current

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

    }

}