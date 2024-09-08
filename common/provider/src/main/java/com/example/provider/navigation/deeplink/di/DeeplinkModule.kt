package com.example.provider.navigation.deeplink.di

import com.example.provider.navigation.deeplink.DeeplinkHandler
import com.example.provider.navigation.deeplink.DeeplinkProcessor
import com.example.provider.navigation.deeplink.DefaultDeeplinkHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DeeplinkModule {
    @Provides
    @Singleton
    fun provideDefaultDeeplinkHandler(
        processors: Set<@JvmSuppressWildcards DeeplinkProcessor>
    ): DeeplinkHandler = DefaultDeeplinkHandler(processors)
}