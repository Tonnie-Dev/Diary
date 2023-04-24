package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.drawable.TintAwareDrawable
import com.uxstate.diary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(scrollBehavior: TopAppBarScrollBehavior,onMenuClicked: () -> Unit) {


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
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                            imageVector = Icons.Default.DateRange,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.home_date_icon)
                    )
                }
            }
    )

}