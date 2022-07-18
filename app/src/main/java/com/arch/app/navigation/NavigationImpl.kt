package com.arch.app.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.arch.app.ui.MainActivity
import com.arch.app.common.navigation.Navigation
import com.arch.app.ui.RouterActivity

class NavigationImpl(private val userHasValidCredentials: Boolean) : Navigation {
    override fun finishSplash(activity: Activity, deepBundle: Bundle?) {
        when {
            userHasValidCredentials ->
                activity.startActivityByName(
                    MainActivity::class.java.name,
                    deepBundle,
                    flags = arrayOf(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_SINGLE_TOP)
                )
            else -> navigateToAuthFlow(activity, bundle = deepBundle)
        }
    }

    override fun startMainActivity(activity: Activity) {
        activity.startActivityByName(MainActivity::class.java.name)
    }

    override fun navigateToAuthFlow(activity: Activity, bundle: Bundle?) {
        //TODO: replace RouterActivity with Auth only for example.
        activity.startActivityByName(RouterActivity::class.java.name, bundle)
    }

    override fun navigateTo(fragment: Fragment, navDirections: NavDirections?, actionId: Int?, bundle: Bundle?) {
        with(fragment.findNavController()) {
            navDirections?.let {
                navigate(it)
            } ?: actionId?.let { navigate(it, bundle) }
        }
    }
}