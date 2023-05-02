package com.uxstate.diary.presentation.screens.write_screen.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.uxstate.diary.R
import com.uxstate.diary.domain.model.GalleryImage
import com.uxstate.diary.domain.model.GalleryState
import com.uxstate.diary.presentation.ui.theme.LocalSpacing
import kotlin.math.max


@Composable
fun GalleryUploader(
    modifier: Modifier = Modifier,
    galleryState: GalleryState,
    imageSize: Dp = 60.dp,
    imageShape: CornerBasedShape = Shapes().medium,
    spaceBetween: Dp = 12.dp,
    onAddClicked: () -> Unit,
    onImageSelected: (Uri) -> Unit,
    onImageClicked: (GalleryImage) -> Unit
) {
    /*
    - variable to trigger the photo picker
    - photo picker allows us to choose multiple images from the gallery
    - in compose we use rememberLauncherForActivityResult
    - for the contract we pass ActivityResultContracts.PickMultipleVisualMedia
    - we then specify the max number of images to pick for each session
    - we can use a trailing lambda for onResult parameter
    - inside the lambda we have a List<Uri>
    - we loop through the uris triggering onImageSelected lambda
    - onImageSelected will be used later to add image to the list in Gallery State
    * */
    val multiplePhotoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 7),
            onResult = { uris ->
                uris.forEach { imageUri ->
                    onImageSelected(imageUri)
                }
            }
    )

    val context = LocalContext.current
    val spacing = LocalSpacing.current


    BoxWithConstraints(modifier = modifier) {

        val numberOfVisibleImages by remember {
            derivedStateOf {

                max(
                        a = 0,
                        b = this.maxWidth.div(spaceBetween + imageSize)
                                .toInt()
                                .minus(2)
                )
            }
        }

        val remainingImages = remember {
            derivedStateOf {
                galleryState.images.size - numberOfVisibleImages
            }
        }

        Row {

            galleryState.images.take(numberOfVisibleImages)
                    .forEach { image ->

                        val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                        .data(image)
                                        .crossfade(true)
                                        .build()
                        )

                        Image(
                                painter = painter,
                                contentDescription = stringResource(id = R.string.gallery_image_text),
                                modifier = Modifier
                                        .clip(imageShape)
                                        .size(imageSize)
                        )
                    }
        }
    }
}