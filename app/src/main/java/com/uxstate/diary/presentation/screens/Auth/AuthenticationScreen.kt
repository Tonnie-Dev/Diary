package com.uxstate.diary.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.uxstate.diary.R
import com.uxstate.diary.presentation.screens.Auth.components.GoogleButton
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Destination
@RootNavGraph(start = true)



@Composable
fun AuthenticationScreen() {

    val spacing = LocalSpacing.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {


        Spacer(modifier = Modifier.height(spacing.spaceLarge))
        Image(
                painter = painterResource(id = R.drawable.google_logo),
                modifier = Modifier.size(spacing.spaceLarge),
                contentDescription = stringResource(
                        id = R.string.google_logo_cdescription
                )
        )
        Text(
                text = stringResource(R.string.welcome_text),
                style = MaterialTheme.typography.bodyMedium
        )
        Text(

                text = stringResource(R.string.sign_in_to_continue_text),
                style = MaterialTheme.typography.bodySmall
        )

        GoogleButton(modifier = Modifier.fillMaxWidth(.8f)) {}
    }

}


@Preview
@Composable
fun AuthenticationScreenPrev() {
    AuthenticationScreen()
}
