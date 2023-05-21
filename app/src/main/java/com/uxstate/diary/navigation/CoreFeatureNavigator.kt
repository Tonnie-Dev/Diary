package com.uxstate.diary.navigation

import androidx.navigation.NavController
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.uxstate.auth.AuthNavigator
import com.uxstate.diary.presentation.screens.destinations.HomeScreenDestination

class CoreFeatureNavigator(
    private val navController: NavController,
    private val navGraph: NavGraphSpec
) : AuthNavigator {
    override fun navigateToHome() {

        navController.navigate(HomeScreenDestination within navGraph)
    }
}