package com.emtelco.pokeapiemtelco.di

import android.content.Context
import com.emtelco.pokeapiemtelco.core.AuthHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideBiometricAuthHelper(@ApplicationContext context: Context): AuthHelper {
        return AuthHelper(context)
    }

}