package com.uxstate.home.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.uxstate.ui.R
import com.uxstate.model.Diary
import com.uxstate.model.Mood
import com.uxstate.ui.theme.DiaryTheme
import com.uxstate.ui.theme.LocalElevation
import com.uxstate.ui.theme.LocalSpacing
import com.uxstate.util.fetchImagesFromFirebase
import com.uxstate.util.toInstant
import io.realm.kotlin.ext.realmListOf


 @Composable
internal fun DiaryHolder(diary: Diary, onClickDiary: (objectId: String) -> Unit) {

    val spacing = LocalSpacing.current
    val elevation = LocalElevation.current
    val context = LocalContext.current

    var isGalleryOpen by remember {
        mutableStateOf(false)
    }

    var isGalleryLoading by remember {
        mutableStateOf(false)
    }
    //we will calculate the height of the side line from the component height

    var componentHeight by remember { mutableStateOf(spacing.spaceDefault) }

    // get local density from composable
    val density = LocalDensity.current

    val downloadedImages = remember { mutableListOf<Uri>() }

    //to be triggered each time we open/hide showGallery
    LaunchedEffect(key1 = isGalleryOpen, block = {


        if (isGalleryOpen && downloadedImages.isEmpty()) {

            isGalleryLoading = true

            //from utils

            fetchImagesFromFirebase(
                    remoteImagesPaths = diary.images, //pass remote paths i.e. RealmList<String>,
                    onImageDownload = { uri ->

                        //store the url within downloadedImage list property
                        downloadedImages.add(uri)
                    },
                    onImageDownloadFailed = {

                        Toast.makeText(
                                context, """
                            Images not uploade yet, wait a little bit or try uploading again
                            """.trimIndent(), Toast.LENGTH_SHORT
                        )
                                .show()

                        //reset isLoading and isOpen back to false
                        isGalleryLoading = false
                        isGalleryOpen = false
                    },
                    onReadyToDisplay = {

                        /*When readay to display image
                        - isLoading is changed to false
                        - isOpen remains true
                        * */
                        isGalleryLoading = false
                        isGalleryOpen = true
                    })
        }

    })

    Row(modifier = Modifier
            .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
            )
            {
                onClickDiary(diary._id.toHexString())
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
                            isGalleryLoading = isGalleryLoading,
                            onToggleGallery = { isGalleryOpen = !isGalleryOpen }
                    )
                }

                AnimatedVisibility(
                        visible = isGalleryOpen && !isGalleryLoading,
                        enter = fadeIn() + expandVertically(
                                animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                )
                        )
                ) {

                    //column to apply padding
                    Column(modifier = Modifier.padding(spacing.spaceMedium)) {

                        Gallery(images = downloadedImages)
                    }
                }


            }
        }
    }

}

@Composable
internal fun ShowGalleryButton(
    isGalleyOpen: Boolean, isGalleryLoading: Boolean,
    onToggleGallery: () -> Unit
) {

    TextButton(onClick = onToggleGallery) {
        Text(
                text = if (isGalleyOpen)

                    if (isGalleryLoading)
                        stringResource(R.string.loading_text)
                    else stringResource(R.string.hide_gallery_action)
                else stringResource(R.string.show_gallery_action),
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
internal fun DiaryHolderPreview() {
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun DiaryHolderPreviewDark() {
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

