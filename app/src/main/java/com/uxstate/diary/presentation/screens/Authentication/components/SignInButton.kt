package com.uxstate.diary.presentation.screens.Authentication.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.uxstate.diary.R
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Composable
fun SignInButton(modifier: Modifier = Modifier, onClick: () -> Unit) {


    val spacing = LocalSpacing.current
    OutlinedButton(onClick = onClick, modifier = modifier, shape = RectangleShape) {



            Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = stringResource(
                            R.string.google_logo_cdescription
                    )
            )

            Spacer(modifier = Modifier.width(spacing.spaceSmall))

            Text(
                    text = stringResource(R.string.sign_in_google_text),
                    style = MaterialTheme.typography.bodySmall
            )

        }
    }



@Preview
@Composable
fun SignInButtonPrev() {
    SignInButton(modifier = Modifier.fillMaxWidth(.9f)) {}
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignInButtonPrevDark() {
    SignInButton(modifier = Modifier.fillMaxWidth(.9f)) {}
}