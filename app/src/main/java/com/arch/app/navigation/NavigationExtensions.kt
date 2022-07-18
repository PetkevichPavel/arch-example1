package com.arch.app.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.arch.app.R
import com.arch.app.common.utils.safe

/**
 * String extension function, load Intent for future start.
 * @return Intent if it's prepared, otherwise null.
 */
internal fun String.loadIntentOrNull(context: Context, bundle: Bundle? = null): Intent? =
    safe {
        Class.forName(this).run {
            bundle?.let {
                Intent(context, this).apply {
                    putExtra("$canonicalName-bundle", bundle)
                }
            } ?: Intent(context, this)
        }
    }

/**
 * Activity extension function, start activity via @param[canonicalName].
 * @param[canonicalName] - canonical name of the target activity.
 * @param[bundle] - bundle for target activity can be null.
 * @param[isPrevFinish] - label if the previous activity should be killed or leaved, by default true.
 * @param[flags] - is optional, in case you need to add flags before activy will start add them here as arrayOf(Int).
 */
fun Activity.startActivityByName(
    canonicalName: String,
    bundle: Bundle? = null,
    isPrevFinish: Boolean = true,
    flags: Array<Int>? = null
) {
    canonicalName.loadIntentOrNull(this, bundle)?.let { intent ->
        flags?.forEach { intent.addFlags(it) }
        startActivity(intent)
        if (isPrevFinish) finish()
    }
}

/**
 * Activity extension function, for navigation between fragments.
 * @param navHostFragmentId - by default is the main navigation graph nav_host_fragment.
 * @param resId - an {@link NavDestination#getAction(int) action} id or a destination id to
 *              navigate to
 * @param args arguments to pass to the destination.
 * @param navOptions special options for this navigation operation.
 */
fun Activity.navigateTo(
    navHostFragmentId: Int = R.id.nav_host_fragment,
    resId: Int,
    navOptions: NavOptions? = null,
    args: Bundle? = null
) {
    findNavController(navHostFragmentId).navigate(resId, args, navOptions)
}

/**
 * Activity extension function, check if is currentDestination same as requested destination.
 * @param id - requested destination.
 * @param navHostFragmentId - by default is the main navigation graph nav_host_fragment.
 * @return [Boolean] in case of same destination true otherwise false.
 */
fun Activity.isItCurrentFragment(id: Int, navHostFragmentId: Int = R.id.nav_host_fragment) =
    findNavController(navHostFragmentId).currentDestination?.id == id

/**
 * Add destination graph with [destinationGraphName] into current graph.
 * @sample addDestinationGraph(navController, destinationGraphName)?.let {navController.navigate(R.id.someFrag)}
 * @param navController - current navigation controller.
 * @param destinationGraphName - destination graph name.
 * @return NavGraph? - return null or graph in case of existence.
 */
private fun Activity.addDestinationGraph(
    navController: NavController,
    destinationGraphName: String
): NavGraph? {
    var destinationGraph: NavGraph? = null
    // Find the resourceId of the graph we want to attach
    val navigationId = resources.getIdentifier(destinationGraphName, "navigation", packageName)
    navigationId.takeIf { it != 0 }?.apply {
        // inflate the graph using the id obtained above
        destinationGraph = navController.navInflater.inflate(this)
    }
    destinationGraph?.let { navController.graph.addDestination(it) }
    return destinationGraph
}

/**
 * Activity extension function, for setting in nav_graph the launcher fragment programmatically.
 * @param navHostFragmentId - main nav host fragment id.
 * @param destinationId - destination id.
 */
fun Activity.setLauncherFragment(
    navHostFragmentId: Int,
    destinationId: Int,
    graphResId: Int,
    startDestinationArgs: Bundle? = null
) {
    findNavController(navHostFragmentId).apply {
        val navGraph = navInflater.inflate(graphResId)
        navGraph.setStartDestination(destinationId)
        setGraph(navGraph, startDestinationArgs)
    }
}

/**
 * Activity extension function, for setting in nav_graph the launcher fragment programmatically.
 * @param navHostFragmentId - main nav host fragment id.
 * @param destinationId - destination id.
 */
fun Activity.setStartDestination(
    navHostFragmentId: Int,
    destinationId: Int,
    startDestinationArgs: Bundle? = null
) {
    findNavController(navHostFragmentId).apply {
        graph.apply {
            graph.setStartDestination(destinationId)
        }.also {
            setGraph(it, startDestinationArgs)
        }
    }
}

/**
 * AppCompatActivity extension function for set up graph.
 * @param navHostFrgResId - navigation host fragment id.
 * @param graphResId - graph resource id.
 * @param args - bundle.
 */
fun AppCompatActivity.setNavGraph(
    @IdRes navHostFrgResId: Int,
    @NavigationRes graphResId: Int,
    args: Bundle
) {
    (supportFragmentManager.findFragmentById(navHostFrgResId) as? NavHostFragment)?.apply {
        navController.apply {
            setGraph(navInflater.inflate(graphResId), args)
        }
    }
}
