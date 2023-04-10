package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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

            //this is the scaffold passed as a composable fun
            content = content,

            //content defined belo
            drawerContent = {
                ModalDrawerSheet() {
                    NavDrawerContent(onSignOutClicked = onSignOutClicked)
                }

            }
    )
}


@Composable
fun NavDrawerContent(onSignOutClicked: () -> Unit) {
    val spacing = LocalSpacing.current

    //use box to center logo
    Box(
            modifier = Modifier
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center
    ) {

        Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_logo)
        )

    }

    //Drawer Menu Item
    NavigationDrawerItem(
            label = {
                Row(
                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = stringResource(
                                    id = R.string.google_logo
                            )
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceMedium))
                    Text(
                            text = stringResource(R.string.sign_out),
                            style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                    )
                }
            },
            selected = false,
            onClick = onSignOutClicked
    )

}
