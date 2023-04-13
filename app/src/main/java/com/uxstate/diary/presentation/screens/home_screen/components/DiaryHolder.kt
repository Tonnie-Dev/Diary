package com.uxstate.diary.presentation.screens.home_screen.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.domain.model.Mood
import com.uxstate.diary.presentation.ui.theme.DiaryTheme
import com.uxstate.diary.presentation.ui.theme.LocalElevation
import com.uxstate.diary.presentation.ui.theme.LocalSpacing
import com.uxstate.diary.util.toInstant

@Composable
fun DiaryHolder(diary: Diary, onClickDiary: (objectId: String) -> Unit) {

    val spacing = LocalSpacing.current
    val elevation = LocalElevation.current

    //we will calculate the height of the side line from the component height

  //  var componentHeight by remember { mutableStateOf(spacing.spaceDefault) }

    var componentHeight by remember { mutableStateOf(spacing.spaceDefault) }

    // get local density from composable
    val density = LocalDensity.current

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

        //Diary Entry wrapped in surface to apply tonal elevation

        Surface(
                modifier = Modifier
                        .clip(shape = Shapes().medium)
                        .onGloballyPositioned {
                            componentHeight = with(density) { it.size.height.toDp() }
                        }, tonalElevation = elevation.level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                DiaryHeader(moodName = diary.mood, time = diary.date.toInstant())

                Text(
                        text = diary.description,
                        modifier = Modifier.padding(spacing.spaceMedium),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DiaryHolderPreview() {
    DiaryTheme() {
        DiaryHolder(diary = Diary().apply {
            title = "My Day"
            mood = Mood.ANGRY.name
            description = """
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,
                molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum
                numquam blanditiis harum quisquam eius sed odit fugiat iusto 
            """.trimIndent()
        }, onClickDiary = {})
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DiaryHolderPreviewDark() {
    DiaryTheme() {
        DiaryHolder(diary = Diary().apply {
            title = "My Day"
            mood = Mood.TENSE.name
            description = """
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,
                molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum
                numquam blanditiis harum quisquam eius sed odit fugiat iusto 
            """.trimIndent()
        }, onClickDiary = {})
    }
}