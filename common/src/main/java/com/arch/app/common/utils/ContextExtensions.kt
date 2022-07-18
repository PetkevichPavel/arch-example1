package com.arch.app.common.utils

import android.app.NotificationManager
import android.content.Context


/**
 * Returns the NotificationManager instance.
 */
val Context.notificationManager: NotificationManager?
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
