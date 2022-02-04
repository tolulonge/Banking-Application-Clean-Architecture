package org.drulabs.bankbuddy.application

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.drulabs.bankbuddy.di.DaggerBankBuddyAppComponent

class BankBuddyApp: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerBankBuddyAppComponent.builder().application(this).build()
    }
}