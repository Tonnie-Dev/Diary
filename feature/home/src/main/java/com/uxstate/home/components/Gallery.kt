package com.uxstate.home.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.uxstate.ui.R
import kotlin.math.max

@Composable
internal fun Gallery(
    modifier: Modifier = Modifier,
    images: List<Uri>,
    imageSize: Dp = 40.dp,
    spaceBetween: Dp = 10.dp,
    imageShape: CornerBasedShape = Shapes().small
) {

    val context = LocalContext.current

    //flexible, expandable
    BoxWithConstraints(modifier = modifier) {

        //cache and retain value across recompositions
        val numberOfVisibleImages by remember {

            //Buffer to prevent recomposition once the value changes
            derivedStateOf {

                max(
                        a = 0,
                        b = this.maxWidth.div(imageSize + spaceBetween)
                                .toInt()
                                .minus(1)
                )
            }
        }


        val remainingImages by remember {
            derivedStateOf { images.size - numberOfVisibleImages }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spaceBetween)) {
            images.take(numberOfVisibleImages)
                    .forEach {

                        image ->

                        val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context = context)
                                        .data(image)
                                        .crossfade(true)
                                        .build()
                        )

                        Image(
                                painter = painter,
                                contentDescription = stringResource(R.string.gallery_image_text),
                                modifier = Modifier
                                        .clip(imageShape)
                                        .size(imageSize),
                                contentScale = ContentScale.Crop
                        )
                    }

            if (remainingImages > 0) {

                LastImageOverlay(
                        imageSize = imageSize,
                        remainingImages = remainingImages,
                        imageShape = imageShape
                )
            }
        }

    }
}