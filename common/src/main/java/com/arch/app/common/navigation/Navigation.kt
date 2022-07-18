package com.arch.app.common.navigation

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections

interface Navigation {
    fun finishSplash(activity: Activity, deepBundle: Bundle? = null)
    fun startMainActivity(activity: Activity)
    fun navigateToAuthFlow(activity: Activity, bundle: Bundle? = null)
    fun navigateTo(fragment: Fragment, navDirections: NavDirections? = null, actionId: Int? = null, bundle: Bundle? = null)
}