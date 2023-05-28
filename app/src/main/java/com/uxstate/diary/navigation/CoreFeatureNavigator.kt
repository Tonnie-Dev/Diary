package com.uxstate.diary.navigation

import androidx.navigation.NavController
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.uxstate.auth.AuthScreenNavigator
import com.uxstate.auth.AuthenticationScreen
import com.uxstate.auth.destinations.AuthenticationScreenDestination
import com.uxstate.home.HomeScreenNavigator
import com.uxstate.home.destinations.HomeScreenDestination
import com.uxstate.write.WriteScreenNavigator
import com.uxstate.write.destinations.WriteScreenDestination


class CoreFeatureNavigator(
    private val navController: NavController,
    private val navGraph: NavGraphSpec
) : AuthScreenNavigator, HomeScreenNavigator, WriteScreenNavigator {
    override fun navigateToHomeScreen() {

        navController.navigate(HomeScreenDestination )
    }

    override fun navigateToWriteScreen(id: String?) {
        navController.navigate(WriteScreenDestination(id)
        )
    }

    override fun navigateBackToAuthScreen() {
        navController.navigate(AuthenticationScreenDestination)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun navigateBackToHomeScreen() {
        navController.navigateUp()
    }

}