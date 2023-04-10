package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.uxstate.diary.R
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@Composable
fun DiaryNavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
            drawerState = drawerState,
            content = content,
            drawerContent = {
                NavDrawerContent(onSignOutClicked = onSignOutClicked)

            }
    )
}


@Composable
fun NavDrawerContent(onSignOutClicked: () -> Unit) {
    val spacing = LocalSpacing.current
    Box(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(spacing.spaceTwoHundredFifty), contentAlignment = Alignment.Center
    ) {

        Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_logo)
        )
    }

    //Drawer Menu Item
    NavigationDrawerItem(
            label = {
                Row(modifier = Modifier.padding(horizontal = spacing.spaceMedium)) {
                    Icon(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = stringResource(
                                    id = R.string.google_logo
                            )
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceTwoHundred))
                    Text(text = stringResource(R.string.sign_out))
                }
            },
            selected = false,
            onClick = onSignOutClicked
    )
}
