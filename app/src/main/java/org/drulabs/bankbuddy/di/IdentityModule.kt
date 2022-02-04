package org.drulabs.bankbuddy.di

import dagger.Module
import dagger.Provides
import org.drulabs.bankbuddy.BuildConfig
import org.drulabs.bankbuddy.presentation.qualifier.UserIdentity

@Module
class IdentityModule {

    @Provides
    @UserIdentity
    fun providesUserID(): String = BuildConfig.USER_ID
}