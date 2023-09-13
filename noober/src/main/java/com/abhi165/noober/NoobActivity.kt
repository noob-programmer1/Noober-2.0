package com.abhi165.noober

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import com.abhi165.noober.ui.NoobScreen
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent

internal class NoobActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val route = intent.getStringExtra("route")
        NoobHelper.isNooberVisible = true
        NoobHelper.notificationManager.cancelNotification()
        setContent {
            MaterialTheme {
                NoobScreen(route = route)
            }
        }

        val intent: Intent = intent
        val action: String? = intent.action
        val data: Uri? = intent.data
        if (Intent.ACTION_VIEW == action) {
           if (DeepLinkHandler.handleDeepLink(data))
            Toast.makeText(this, "Account Switched Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NoobHelper.isNooberVisible = false
        NoobHelper.notificationManager.showNotification("")
    }
}
