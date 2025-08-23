package com.emtelco.pokeapiemtelco.di

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @Singleton
    fun provideActivity(activity: Activity): FragmentActivity {
        return activity as FragmentActivity
    }

}