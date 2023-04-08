package com.uxstate.diary.presentation.screens.Auth.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uxstate.diary.R
import com.uxstate.diary.presentation.ui.theme.DiaryTheme
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    loadingState: Boolean = false,
    primaryText: String = "Sign in with Google",
    secondaryText: String = "Please wait ...",
    icon: Int = R.drawable.google_logo,
    shape: Shape = Shapes().small,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    borderStrokeWidth: Dp = 1.dp,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    var buttonText by remember { mutableStateOf(primaryText) }

    LaunchedEffect(key1 = loadingState, block = {

        buttonText = if (loadingState) primaryText else secondaryText
    })


    Surface(
            modifier = modifier.clickable(enabled = !loadingState) { onClick() },
            shape = shape,
            border = BorderStroke(width = spacing.spaceSingleDp, color = borderColor),
            color = backgroundColor

    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.spaceMedium)
                        .animateContentSize(
                                animationSpec = tween(
                                        durationMillis = 3_00,
                                        easing = LinearOutSlowInEasing
                                )
                        ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(id = R.string.google_logo_cdescription),
                    tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            Text(
                    text = buttonText,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )

            if (loadingState) {
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                CircularProgressIndicator(
                        modifier = Modifier.size(spacing.spaceMedium),
                        strokeWidth = spacing.spaceDoubleDp,
                        color = progressIndicatorColor
                )
            }
        }
    }





}


@Preview
@Composable
fun SignInButtonPrev() {
    DiaryTheme() {
        GoogleButton(loadingState = true) {}
    }
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignInButtonPrevDark() {

    DiaryTheme() {
        GoogleButton(loadingState = true) {}
    }

}