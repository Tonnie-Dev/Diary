package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.uxstate.diary.R
import com.uxstate.ui.theme.LocalSpacing


@Composable
fun DiaryNavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onDeleteAllDiaries: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(drawerState = drawerState,

            //this is the scaffold passed as a composable fun
            content = content,

            //content defined belo
            drawerContent = {
                ModalDrawerSheet() {
                    NavDrawerContent(
                            onSignOutClicked = onSignOutClicked,
                            onDeleteAllDiaries = onDeleteAllDiaries
                    )
                }

            })
}


@Composable
fun NavDrawerContent(onSignOutClicked: () -> Unit, onDeleteAllDiaries: () -> Unit) {
    val spacing = LocalSpacing.current

    //use box to center logo
    Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {

        Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_logo)
        )

    }

    //Drawer Menu Item 1
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
                            text = stringResource(R.string.sign_out), style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                    )
                }
            }, selected = false, onClick = onSignOutClicked
    )

    //Drawer Menu Item 1
    NavigationDrawerItem(
            label = {

                Row(
                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_all_icon),
                            tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))

                    Text(
                            text = "Delete All Diaries", style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                    )
                }

            }, selected = false, onClick = onDeleteAllDiaries
    )

}
