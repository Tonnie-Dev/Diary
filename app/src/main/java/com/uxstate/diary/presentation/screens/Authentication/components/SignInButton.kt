package com.uxstate.diary.presentation.screens.Authentication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.uxstate.diary.R

@Composable
fun SignInButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(onClick = onClick, modifier = modifier) {
        Row() {


            Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = stringResource(
                            R.string.google_logo_cdescription
                    )
            )
        }
    }
}