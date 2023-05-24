package com.uxstate.diary.navigation

import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.dynamic.routedIn
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
object NavGraphs {
    //Auth Module NavGraph - defines navigation graph by instantiating NavGraphSpecs
    val auth = object : NavGraphSpec {

        override val route = "auth"
        override val startRoute = AuthenticationScreenDestination
        override val destinationsByRoute=
            listOf<DestinationSpec<*>>(AuthenticationScreenDestination)
                .associateBy{ it.route }
    }

    //Home NavGraph

    val home = object : NavGraphSpec {

        override val route = "home"
        override val startRoute  =HomeScreenDestination
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
        override val destinationsByRoute = emptyMap<String,DestinationSpec<*>>()
        override val route = "root"
        override val startRoute = auth
        override val nestedNavGraphs = listOf(auth, home, write)
    }
}


fun ArrayDeque<NavBackStackEntry>.print(prefix: String = "stack") {
    val stack = toMutableList()
            .map { it.destination.route }
            .toTypedArray().contentToString()
    println("$prefix = $stack")
}

fun DestinationScope<*>.currentNavigator(openSettings: () -> Unit): CoreFeatureNavigator{
    return CoreFeatureNavigator(
            navBackStackEntry.destination.navGraph(),
            navController,
            openSettings
    )
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DestinationsNavHost(
            engine = rememberAnimatedNavHostEngine(
                    rootDefaultAnimations = RootNavGraphDefaultAnimations(
                            enterTransition = { defaultTiviEnterTransition(initialState, targetState) },
                            exitTransition = { defaultTiviExitTransition(initialState, targetState) },
                            popEnterTransition = { defaultTiviPopEnterTransition() },
                            popExitTransition = { defaultTiviPopExitTransition() },
                    )
            ),
            navController = navController,
            navGraph = NavGraphs.root,
            modifier = modifier,
            dependenciesContainerBuilder = {
                dependency(currentNavigator(onOpenSettings))
            }
    )
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviEnterTransition(
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
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviExitTransition(
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
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}