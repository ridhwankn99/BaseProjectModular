package com.example.home.deeplink

import android.content.Context
import android.content.Intent
import com.example.home.ui.HomeActivity
import com.example.provider.navigation.deeplink.DeeplinkProcessor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomePresentationDeeplinkProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) : DeeplinkProcessor {
    override fun matches(deeplink: String): Boolean {
        if (deeplink.contains("/home")) {
            return true
        }
        return false
    }

    override fun execute(deeplink: String) {
        context.startActivity(Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}