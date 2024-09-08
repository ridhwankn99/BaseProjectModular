package com.example.login.deeplink

import android.content.Context
import android.content.Intent
import com.example.login.ui.LoginActivity
import com.example.provider.navigation.deeplink.DeeplinkProcessor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoginPresentationDeeplinkProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) : DeeplinkProcessor {
    override fun matches(deeplink: String): Boolean {
        if (deeplink.contains("/login")) {
            return true
        }
        return false
    }

    override fun execute(deeplink: String) {
        context.startActivity(Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}