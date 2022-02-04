package org.drulabs.bankbuddy.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import org.drulabs.bankbuddy.application.BankBuddyApp
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DomainModule::class,
        DataModule::class,
        LocalPersistenceModule::class,
        RemoteModule::class,
        IdentityModule::class,
        PresentationModule::class,
        AppModule::class
    ]
)
interface BankBuddyAppComponent : AndroidInjector<BankBuddyApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): BankBuddyAppComponent
    }

    override fun inject(app: BankBuddyApp)
}