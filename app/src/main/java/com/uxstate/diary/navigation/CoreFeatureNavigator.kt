package com.uxstate.diary.navigation

import androidx.navigation.NavController
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.uxstate.auth.AuthNavigator
import com.uxstate.auth.AuthScreenNavigator
import com.uxstate.home.HomeNavigator
import com.uxstate.home.HomeScreenNavigator
import com.uxstate.write.WriteScreenNavigator


class CoreFeatureNavigator(
    private val navController: NavController,
    private val navGraph: NavGraphSpec
) : AuthScreenNavigator, HomeScreenNavigator, WriteScreenNavigator {
    override fun navigateToHome() {

        //navController.navigate(HomeScreenDestination within navGraph)
    }
}