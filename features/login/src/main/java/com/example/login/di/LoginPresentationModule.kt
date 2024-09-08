package com.example.login.di

import com.example.login.deeplink.LoginPresentationDeeplinkProcessor
import com.example.provider.navigation.deeplink.DeeplinkProcessor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
interface LoginPresentationModule {
    @Binds
    @IntoSet
    fun bindsHomeDeeplinkProcessorIntoSet(
        processor: LoginPresentationDeeplinkProcessor
    ): DeeplinkProcessor
}