package com.uxstate.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.uxstate.ui.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClicked: () -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit
) {


    val dateDialog = rememberUseCaseState()
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    TopAppBar(scrollBehavior = scrollBehavior,
            navigationIcon = {
                IconButton(onClick = onMenuClicked) {
                    Icon(
                            imageVector = Icons.Default.Menu,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.home_menu_icon)
                    )
                }
            },
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                if (dateIsSelected) {
                    IconButton(onClick = onDateReset) {
                        Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.close_icon),
                                tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                } else {

                    IconButton(onClick = { //show calendar

                        dateDialog.show()
                    }) {
                        Icon(
                                imageVector = Icons.Default.DateRange,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = stringResource(R.string.home_date_icon)
                        )
                    }
                }
            }
    )


    CalendarDialog(
            state = dateDialog,

            selection = CalendarSelection.Date { date ->
                pickedDate = date
                onDateSelected(
                        ZonedDateTime.of(
                                pickedDate,
                                LocalTime.now(),
                                ZoneId.systemDefault()
                        )
                )
            }, config = CalendarConfig(monthSelection = true, yearSelection = true)
    )
}