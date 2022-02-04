package org.drulabs.bankbuddy.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.drulabs.bankbuddy.BuildConfig
import org.drulabs.bankbuddy.data.model.TransactionData
import org.drulabs.bankbuddy.data.model.UserInfoData
import org.drulabs.bankbuddy.data.repository.RemoteDataSource
import org.drulabs.bankbuddy.remote.api.BankingService
import org.drulabs.bankbuddy.remote.mapper.Mapper
import org.drulabs.bankbuddy.remote.mapper.TransactionDataNetworkMapper
import org.drulabs.bankbuddy.remote.mapper.UserInfoDataNetworkMapper
import org.drulabs.bankbuddy.remote.model.TransactionNetwork
import org.drulabs.bankbuddy.remote.model.UserInfoNetwork
import org.drulabs.bankbuddy.remote.source.RemoteDataSourceImpl
import org.drulabs.bankbuddy.utils.FakeBankingService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [RemoteModule.Binders::class])
class RemoteModule {

    @Module
    interface Binders {

        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: RemoteDataSourceImpl
        ): RemoteDataSource

        @Binds
        fun bindUserInfoMapper(
            userInfoMapper: UserInfoDataNetworkMapper
        ): Mapper<UserInfoData, UserInfoNetwork>

        @Binds
        fun bindTransactionMapper(
            transactionMapper: TransactionDataNetworkMapper
        ): Mapper<TransactionData, TransactionNetwork>
    }

    @Provides
    fun providesBankingService(retrofit: Retrofit): BankingService =
        FakeBankingService()


    @Provides
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()


}