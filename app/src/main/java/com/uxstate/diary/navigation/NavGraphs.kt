package com.uxstate.diary.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import com.uxstate.diary.presentation.screens.destinations.AuthenticationScreenDestination
import com.uxstate.diary.presentation.screens.destinations.HomeScreenDestination


/*gather all destinations / nav graphs from other modules into a single "top-level"
navigation graph to pass to DestinationsNavHost call. We recommend having a globally
accessible object containing the nav graphs*/
object NavGraphs {
    //Auth Module NavGraph - defines navigation graph by instantiating NavGraphSpecs
    val auth = object : NavGraphSpec {

        override val route = "auth"
        override val startRoute = AuthenticationScreenDestination routedIn this
        override val destinationsByRoute: Map<String, DestinationSpec<*>>(Auth)




    }
}