package org.drulabs.bankbuddy.di

import android.app.Application
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.drulabs.bankbuddy.data.model.TransactionData
import org.drulabs.bankbuddy.data.model.UserInfoData
import org.drulabs.bankbuddy.data.repository.LocalDataSource
import org.drulabs.bankbuddy.local.database.BankBuddyDB
import org.drulabs.bankbuddy.local.mapper.Mapper
import org.drulabs.bankbuddy.local.mapper.TransactionDataLocalMapper
import org.drulabs.bankbuddy.local.mapper.UserInfoDataLocalMapper
import org.drulabs.bankbuddy.local.model.TransactionLocal
import org.drulabs.bankbuddy.local.model.UserInfoLocal
import org.drulabs.bankbuddy.local.source.LocalDataSourceImpl
import javax.inject.Singleton

@Module(includes = [LocalPersistenceModule.Binders::class])
class LocalPersistenceModule {

    @Module
    interface Binders {

        @Binds
        fun bindsLocalDataSource(
            localDataSourceImpl: LocalDataSourceImpl
        ): LocalDataSource

        @Binds
        fun bindUserInfoMapper(
            userInfoMapper: UserInfoDataLocalMapper
        ): Mapper<UserInfoData, UserInfoLocal>

        @Binds
        fun bindTransactionMapper(
            transactionMapper: TransactionDataLocalMapper
        ): Mapper<TransactionData, TransactionLocal>
    }

    @Provides
    @Singleton
    fun providesDatabase(
        application: Application
    ) = Room.inMemoryDatabaseBuilder(application, BankBuddyDB::class.java)
        .allowMainThreadQueries()
        .build()

    @Provides
    @Singleton
    fun providesUserInfoDAO(
        bankBuddyDB: BankBuddyDB
    ) = bankBuddyDB.getUserInfoDao()

    @Provides
    @Singleton
    fun providesTransactionDAO(
        bankBuddyDB: BankBuddyDB
    ) = bankBuddyDB.getTransactionDao()

}
