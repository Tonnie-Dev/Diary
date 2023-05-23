package com.uxstate.diary.navigation

import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.uxstate.auth.destinations.AuthenticationScreenDestination
import com.uxstate.home.destinations.HomeScreenDestination
import com.uxstate.write.destinations.WriteScreenDestination



/*gather all nav graphs from other modules into a single "top-level"
navigation graph to pass to DestinationsNavHost call. We recommend having a globally
accessible object containing the nav graphs*/

//Create a Navigator Interface for each module/feature screen

//Create CoreFeatureNavigator to Override Screens Navigator Interfaces
object NavGraphs {
    //Auth Module NavGraph - defines navigation graph by instantiating NavGraphSpecs
    val auth = object : NavGraphSpec {

        override val route = "auth"
        override val startRoute = AuthenticationScreenDestination routedIn this
        override val destinationsByRoute=
            listOf<DestinationSpec<*>>(AuthenticationScreenDestination)
                .routedIn(this).associateBy{ it.route }


    }

    //Home NavGraph

    val home = object : NavGraphSpec {

        override val route = "home"
        override val destinationsByRoute = listOf<DestinationSpec<*>>(HomeScreenDestination)
                .routedIn(this).associateBy { it.route }


        override val startRoute  =HomeScreenDestination
    }

    //Write NavGraph

    val write = object : NavGraphSpec {
        override val route = "write"
        override val destinationsByRoute = listOf<DestinationSpec<*>>(WriteScreenDestination)
                .routedIn(this).associateBy { it.route }

        override val startRoute = WriteScreenDestination

    }

    val root = object : NavGraphSpec {
        override val destinationsByRoute = emptyMap<String,DestinationSpec<*>>()
        override val route = "root"
        override val startRoute = home
    }
}