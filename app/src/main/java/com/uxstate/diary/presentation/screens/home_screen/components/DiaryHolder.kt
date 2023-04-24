package com.uxstate.diary.presentation.screens.home_screen.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import io.realm.kotlin.ext.realmListOf

@Composable
fun DiaryHolder(diary: Diary, onClickDiary: (objectId: String) -> Unit) {

    val spacing = LocalSpacing.current
    val elevation = LocalElevation.current

    var isGalleryOpen by remember {
        mutableStateOf(false)
    }
    //we will calculate the height of the side line from the component height

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
                if (diary.images.isNotEmpty()) {


                    ShowGalleryButton(
                            isGalleyOpen = isGalleryOpen,
                            onToggleGallery = { isGalleryOpen = !isGalleryOpen }
                    )
                }

                AnimatedVisibility(
                        visible = isGalleryOpen,
                        enter = fadeIn() + expandVertically(
                                animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                )
                        )
                ) {

                    //column to apply padding
                    Column(modifier = Modifier.padding(spacing.spaceMedium)) {

                        Gallery(images = diary.images)
                    }
                }


            }
        }
    }

}

@Composable
fun ShowGalleryButton(isGalleyOpen: Boolean, onToggleGallery: () -> Unit) {

    TextButton(onClick = onToggleGallery) {
        Text(
                text = if (isGalleyOpen) "Hide Gallery" else "Show Gallery",
                style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                modifier = Modifier.animateContentSize(
                        animationSpec = tween(
                                durationMillis = 9_00,
                                easing = LinearOutSlowInEasing
                        )
                )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DiaryHolderPreview() {
    DiaryTheme() {
        DiaryHolder(diary = Diary().apply {
            images = realmListOf("", "")
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
            images = realmListOf("", "")

            title = "My Day"
            mood = Mood.TENSED.name
            description = """
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,
                molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum
                numquam blanditiis harum quisquam eius sed odit fugiat iusto 
            """.trimIndent()
        }, onClickDiary = {})
    }
}

