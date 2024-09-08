package com.example.provider.navigation.deeplink

import javax.inject.Inject

class BaseDeeplinkActivity: DeeplinkActivity() {
    @Inject
    lateinit var injectedLinkHandler: DeeplinkHandler

    override fun getDeeplinkHandler(): DeeplinkHandler {
        return injectedLinkHandler
    }
}