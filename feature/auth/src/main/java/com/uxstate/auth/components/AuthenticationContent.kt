package com.uxstate.auth.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.uxstate.ui.R
import com.uxstate.ui.theme.DiaryTheme
import com.uxstate.ui.theme.LocalSpacing


@Composable
internal fun AuthenticationContent(loadingState: Boolean,  onButtonClicked: () -> Unit) {

    val spacing = LocalSpacing.current
    Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
                modifier = Modifier
                        .weight(.8f)
                        .fillMaxWidth()
                        .padding(spacing.spaceLarge),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                    modifier = Modifier.weight(10f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = stringResource(id = R.string.google_logo),
                        modifier = Modifier.size(spacing.spaceOneTwenty)
                )

                Spacer(modifier = Modifier.height(spacing.spaceMedium))

                Text(
                        text = stringResource(id = R.string.auth_title),
                        style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize)
                )

                Text(
                        text = stringResource(id = R.string.auth_subtitle),
                        style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                )

            }

            Column(modifier = Modifier.weight(2f), verticalArrangement = Arrangement.Bottom) {

                GoogleButton(isLoading = loadingState, onClick = onButtonClicked)
            }

        }




    }
}


@Preview
@Composable
fun AuthenticationContentPrev() {
    DiaryTheme {

        AuthenticationContent(false){}
    }
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AuthenticationContentPrevDark() {
    DiaryTheme {

        AuthenticationContent(true){}
    }
}