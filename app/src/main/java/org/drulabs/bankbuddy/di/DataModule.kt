package org.drulabs.bankbuddy.di

import dagger.Binds
import dagger.Module
import org.drulabs.bankbuddy.data.mapper.Mapper
import org.drulabs.bankbuddy.data.mapper.TransactionDomainDataMapper
import org.drulabs.bankbuddy.data.mapper.UserInfoDomainDataMapper
import org.drulabs.bankbuddy.data.model.TransactionData
import org.drulabs.bankbuddy.data.model.UserInfoData
import org.drulabs.bankbuddy.data.repository.BankingRepositoryImpl
import org.drulabs.bankbuddy.domain.entities.TransactionEntity
import org.drulabs.bankbuddy.domain.entities.UserInfoEntity
import org.drulabs.bankbuddy.domain.repository.BankingRepository

@Module
abstract class DataModule {

    @Binds
    abstract fun bindsRepository(
        repoImpl: BankingRepositoryImpl
    ): BankingRepository

    @Binds
    abstract fun bindsUserMapper(
        mapper: UserInfoDomainDataMapper
    ): Mapper<UserInfoEntity, UserInfoData>

    @Binds
    abstract fun bindsTransactionMapper(
        mapper: TransactionDomainDataMapper
    ): Mapper<TransactionEntity, TransactionData>
}