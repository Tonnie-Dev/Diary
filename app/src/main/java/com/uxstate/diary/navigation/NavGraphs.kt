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
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope
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
        override val destinationsByRoute = listOf<DestinationSpec<*>>(HomeScreenDestination)
                .associateBy { it.route }

    }

    //Write NavGraph

    val write = object : NavGraphSpec {
        override val route = "write"
        override val startRoute = WriteScreenDestination
        override val destinationsByRoute = listOf<DestinationSpec<*>>(WriteScreenDestination)
                .associateBy { it.route }


    }

    val root = object : NavGraphSpec {
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val route = "root"
        override val startRoute = auth
        override val nestedNavGraphs = listOf(auth, home, write)
    }
}


fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = toMutableList()
            .map { it.destination.route }
            .toTypedArray()
            .contentToString()
    println("$prefix = $stack")
}



fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }

    throw RuntimeException("Unknown nav graph for destination $route")
}
@OptIn(ExperimentalMaterialNavigationApi::class)
@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val navHostEngine = rememberAnimatedNavHostEngine(
            rootDefaultAnimations = RootNavGraphDefaultAnimations(
                    enterTransition = {
                        defaultDiaryEnterTransition(initialState, targetState)

                    },
                    exitTransition = {
                        defaultDiaryExitTransition(
                                initialState,
                                targetState
                        )
                    },
                    popEnterTransition = { defaultDiaryPopEnterTransition() },
                    popExitTransition = { defaultDiaryPopExitTransition() },
            )
    )

    DestinationsNavHost(
            engine = navHostEngine,
            navController = navController,
            navGraph = NavGraphs.root,
            modifier = modifier,
            dependenciesContainerBuilder = {
                dependency(currentNavigator())
            }
    )
}

private fun DependenciesContainerBuilder<*>.currentNavigator(): CoreFeatureNavigator {
    return CoreFeatureNavigator(
            navGraph = navBackStackEntry.destination.navGraph(),
            navController = navController
    )
}
/*fun DestinationScope<*>.currentNavigator(): CoreFeatureNavigator {

}*/

@ExperimentalAnimationApi
private fun AnimatedContentTransitionScope<*>.defaultDiaryEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentTransitionScope<*>.defaultDiaryExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentTransitionScope<*>.defaultDiaryPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentTransitionScope<*>.defaultDiaryPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
}