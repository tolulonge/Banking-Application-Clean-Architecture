package org.drulabs.bankbuddy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.drulabs.bankbuddy.domain.entities.TransactionEntity
import org.drulabs.bankbuddy.domain.entities.UserInfoEntity
import org.drulabs.bankbuddy.presentation.factory.ViewModelFactory
import org.drulabs.bankbuddy.presentation.mapper.Mapper
import org.drulabs.bankbuddy.presentation.mapper.TransactionEntityMapper
import org.drulabs.bankbuddy.presentation.mapper.UserInfoEntityMapper
import org.drulabs.bankbuddy.presentation.model.Transaction
import org.drulabs.bankbuddy.presentation.model.UserInfo
import org.drulabs.bankbuddy.presentation.viewmodels.HomeVM
import org.drulabs.bankbuddy.presentation.viewmodels.TransactionsVM

@Module
abstract class PresentationModule {

    @Binds
    abstract fun bindsViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeVM::class)
    abstract fun bindsHomeViewModel(homeVM: HomeVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionsVM::class)
    abstract fun bindsTransactionsViewModel(transactionsVM: TransactionsVM): ViewModel

    @Binds
    abstract fun bindsUserInfoMapper(
        userInfoEntityMapper: UserInfoEntityMapper
    ): Mapper<UserInfoEntity, UserInfo>

    @Binds
    abstract fun bindsTransactionMapper(
        transactionEntityMapper: TransactionEntityMapper
    ): Mapper<TransactionEntity, Transaction>
}