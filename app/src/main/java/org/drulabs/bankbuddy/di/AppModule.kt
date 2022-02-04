package org.drulabs.bankbuddy.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Binds
import dagger.android.ContributesAndroidInjector
import org.drulabs.bankbuddy.ui.home.HomeActivity
import org.drulabs.bankbuddy.ui.transactions.TransactionList


@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @ContributesAndroidInjector
    internal abstract fun contributesMainActivity(): HomeActivity

    @ContributesAndroidInjector
    internal abstract fun contributesTransactionList(): TransactionList

}