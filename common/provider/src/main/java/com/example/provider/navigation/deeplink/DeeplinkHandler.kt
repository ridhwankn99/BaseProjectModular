package com.example.provider.navigation.deeplink

interface DeeplinkHandler {
    fun process(deeplink: String): Boolean
}