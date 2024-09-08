package com.example.provider.navigation.deeplink

interface DeeplinkProcessor {
    fun matches(deeplink: String): Boolean

    fun execute(deeplink: String)

    companion object
}