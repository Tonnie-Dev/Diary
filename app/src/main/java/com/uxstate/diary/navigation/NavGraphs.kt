package com.uxstate.diary.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import com.uxstate.auth.destinations.AuthenticationScreenDestination
import com.uxstate.home.destinations.HomeScreenDestination
import com.uxstate.util.Constants
import com.uxstate.write.destinations.WriteScreenDestination
import io.realm.kotlin.mongodb.App


/*gather all nav graphs from other modules into a single "top-level"
navigation graph to pass to DestinationsNavHost call. We recommend having a globally
accessible object containing the nav graphs*/

//Create a Navigator Interface for each module/feature screen

//Create CoreFeatureNavigator to Override Screens Navigator Interfaces

//Override CoreFeatureNavigator functions
object NavGraphs {

    //Auth Module NavGraph - defines navigation graph by instantiating NavGraphSpecs
    val auth = object : NavGraphSpec {

        override val route = "auth"
        override val startRoute = AuthenticationScreenDestination
        override val destinationsByRoute =
            listOf<DestinationSpec<*>>(AuthenticationScreenDestination)
                    .associateBy { it.route }
    }

    //Home NavGraph

    val home = object : NavGraphSpec {

        override val route = "home"
        override val startRoute = HomeScreenDestination
        override val destinationsByRoute =
            listOf<DestinationSpec<*>>(HomeScreenDestination)
                .associateBy { it.route }

    }

    //Write NavGraph

    val write = object : NavGraphSpec {
        override val route = "write"
        override val startRoute = WriteScreenDestination
        override val destinationsByRoute = listOf<DestinationSpec<*>>(WriteScreenDestination)
                .associateBy { it.route }


    }
    //Root NavGraph
    val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = getStartDestination()
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(auth, home, write)
    }


    //Util fxn to compute Starting screen i.e. Auth or Home
    private fun getStartDestination(): NavGraphSpec {

        //App.create() exists as singleton and can be called severally
        val user = App.create(Constants.APP_ID).currentUser

        return if (user != null && user.loggedIn)
            home
        else
         auth
    }
}








