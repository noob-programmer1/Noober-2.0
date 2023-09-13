package com.abhi165.noober

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.abhi165.noober.ui.components.NavigationRoute

@SuppressLint("StaticFieldLeak")
internal object NoobHelper {
    private var hasNooberStartedAlready = false
    var isNooberVisible = false
    lateinit var prefManager: NoobPrefManager
        private set

    private lateinit var context: Context

    lateinit var notificationManager: NotificationManager
        private set

    fun init(context: Context) {
        if (hasNooberStartedAlready) return

        this.context = context.applicationContext
        prefManager = NoobPrefManager(context)
        notificationManager = NotificationManager(context)
        hasNooberStartedAlready = true

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            NoobRepository.log("Crash Detected :(", throwable.stackTraceToString(), true)
            startNoobActivity(route = NavigationRoute.BottomNavItem.Logs.route)
        }
    }

    private fun startNoobActivity(route: String? = null) {
        context.startActivity(Intent(context, NoobActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            route?.let {
                putExtra("route", it)
            }
        })
    }

    fun share(data: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, data)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share to..")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }
}