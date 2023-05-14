package com.uxstate.diary.presentation.ui_utils


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.uxstate.diary.R
import com.uxstate.ui.theme.LocalSpacing


@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    onDialogConfirmed: () -> Unit
) {
    val spacing = LocalSpacing.current
    if (isDialogOpen) {

        AlertDialog(

                title = {

                    Text(
                            text = title,
                            style = TextStyle(
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                    fontWeight = FontWeight.Bold
                            )
                    )
                },
                text = {
                    Text(
                            text = message,
                            style = TextStyle(
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,

                                    )
                    )
                },

                dismissButton = {
                    OutlinedButton(onClick = onCloseDialog) {
                        Text(text = stringResource(R.string.no_text))
                    }

                },

                confirmButton = {

                    OutlinedButton(onClick = {
                        onDialogConfirmed()
                        onCloseDialog()
                    }) {

                        Text(text = stringResource(R.string.yes_text))
                    }
                },
                onDismissRequest = onCloseDialog
        )
    }

}
