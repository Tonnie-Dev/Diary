package com.uxstate.write.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.uxstate.ui.R
import com.uxstate.ui.theme.LocalElevation


@Composable
fun AddImageButton(imageSize: Dp, imageShape: CornerBasedShape, onClick: () -> Unit) {

    val elevation = LocalElevation.current

    Surface(
            modifier = Modifier
                    .size(imageSize)
                    .clip(imageShape),
            onClick = onClick,
            tonalElevation = elevation.level1
    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_icon_text)
            )
        }
    }
}