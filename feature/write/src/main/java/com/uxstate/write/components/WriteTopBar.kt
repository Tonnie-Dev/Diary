package com.uxstate.write.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.uxstate.diary.R
import com.uxstate.model.Diary
import com.uxstate.ui.components.DisplayAlertDialog
import com.uxstate.util.toInstant
import com.uxstate.util.toStringDate
import com.uxstate.util.toStringDateTime
import com.uxstate.util.toStringTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: Diary?,
    moodName: () -> String,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onUpdateDateTime: (dateTime: ZonedDateTime?) -> Unit
) {

    //Local Date & Time Pair
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    //From Max KeppelerTime picker

    val dateDialog = rememberUseCaseState()
    val timeDialog = rememberUseCaseState()


    val formattedDate by remember(key1 = currentDate) {

        derivedStateOf {

            currentDate.toStringDate()
        }
    }


    val formattedTime by remember(key1 = currentTime) {

        derivedStateOf { currentTime.toStringTime() }

    }
    /* val selectedDiaryDateTime = remember(selectedDiary) {
         if (selectedDiary != null) {
             DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.getDefault())
                     .withZone(ZoneId.systemDefault())
                     .format(selectedDiary.date.toInstant())
         } else "Unknown"}*/

    val selectedDiaryDateTime = remember(key1 = selectedDiary) {

        selectedDiary?.date?.toInstant()
                ?.toStringDateTime()
                ?.uppercase() ?: "Unknown"


    }


    var isDateTimeUpdated by remember {
        mutableStateOf(false)
    }
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
                    text = moodName(),
                    style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold
                    ), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )

            Text(
                    text = if (selectedDiary != null && isDateTimeUpdated)
                        "$formattedDate $formattedTime"
                    else if (selectedDiary != null)
                        selectedDiaryDateTime
                    else
                        "$formattedDate $formattedTime",
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
            )
        }
    }, actions = {


        if (isDateTimeUpdated) {
            IconButton(onClick = {
                dateDialog.show()
                currentDate = LocalDate.now()
                currentTime = LocalTime.now()
                isDateTimeUpdated = false

                //reset date
                onUpdateDateTime(null)
            }) {
                Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close_icon),
                        tint = MaterialTheme.colorScheme.onSurface
                )
            }


        } else {
            IconButton(onClick = { dateDialog.show() }) {
                Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.date_icon),
                        tint = MaterialTheme.colorScheme.onSurface
                )
            }

        }


        if (selectedDiary != null) {
            DeleteDiaryAction(diary = selectedDiary, onDeleteConfirmed = onDeleteConfirmed)
        }
    })
    val boundary = LocalDate.now().minusYears(1)..LocalDate.now()

    CalendarDialog(
            state = dateDialog,
            selection = CalendarSelection.Date { localDate ->
                currentDate = localDate
                timeDialog.show()
            },
            config = CalendarConfig(boundary = boundary)
    )

    //triggered after Date Selection
    ClockDialog(
            state = timeDialog,
            selection = ClockSelection.HoursMinutes { hours, minutes ->
                currentTime = LocalTime.of(hours, minutes)
                isDateTimeUpdated = true
                onUpdateDateTime(ZonedDateTime.of(currentDate, currentTime, ZoneId.systemDefault()))
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