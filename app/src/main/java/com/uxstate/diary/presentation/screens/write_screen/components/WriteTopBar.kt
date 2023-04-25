package com.uxstate.diary.presentation.screens.write_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.uxstate.diary.R
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.presentation.screens.home_screen.components.DisplayAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(selectedDiary: Diary?, onBackPressed: () -> Unit, onDeleteConfirmed: () -> Unit) {
    CenterAlignedTopAppBar(navigationIcon = {
        IconButton(onClick = onBackPressed) {

            Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_arrow_text)
            )
        }
    }, title = {

        Column {
            Text(
                    text = "Happy", style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
            ), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )

            Text(
                    text = "25 APR 2023, 7:13 AM",
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
            )
        }
    }, actions = {

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.date_icon),
                    tint = MaterialTheme.colorScheme.onSurface
            )
        }

        if (selectedDiary != null) {
            DeleteDiaryAction(diary = selectedDiary, onDeleteConfirmed = onDeleteConfirmed)
        }
    })
}


@Composable
fun DeleteDiaryAction(diary: Diary?, onDeleteConfirmed: () -> Unit) {

    var isExpanded by remember { mutableStateOf(false) }

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
        DropdownMenuItem(
                text = { Text(text = stringResource(R.string.delete_action_text)) },
                onClick = {
                    isDialogOpen = true
                    isExpanded = false
                })


    }

    DisplayAlertDialog(
            title = stringResource(id = R.string.delete_action_text),
            message = """
                            Are you sure you want to permanently delete this Diary entry '${diary?.title}'?
                        """.trimIndent(),

            isDialogOpen = isDialogOpen,
            onCloseDialog = { isDialogOpen = false },
            onDialogConfirmed = onDeleteConfirmed
    )

    IconButton(onClick = { isExpanded = !isExpanded }) {
        Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.overflow_menu_icon),
                tint = MaterialTheme.colorScheme.onSurface
        )
    }

}