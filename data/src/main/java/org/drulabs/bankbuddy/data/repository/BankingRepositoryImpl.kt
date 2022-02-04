package org.drulabs.bankbuddy.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import org.drulabs.bankbuddy.data.mapper.Mapper
import org.drulabs.bankbuddy.data.model.TransactionData
import org.drulabs.bankbuddy.data.model.UserInfoData
import org.drulabs.bankbuddy.domain.entities.TransactionEntity
import org.drulabs.bankbuddy.domain.entities.UserInfoEntity
import org.drulabs.bankbuddy.domain.repository.BankingRepository
import javax.inject.Inject

class BankingRepositoryImpl @Inject constructor(
    private val userInfoMapper: Mapper<UserInfoEntity, UserInfoData>,
    private val transactionMapper: Mapper<TransactionEntity, TransactionData>,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : BankingRepository {

    override fun getUserInfo(identifier: String): Observable<UserInfoEntity> {

        val userInfoObservable = localDataSource.getUserInfo(identifier)
            .map { userInfoMapper.from(it) }

        return remoteDataSource.getUserInfo(identifier)
            .map {
                localDataSource.saveUserInfo(it)
                userInfoMapper.from(it)
            }.onErrorResumeNext(Observable.empty())
            .concatWith(userInfoObservable)
    }

    override fun getUserTransactions(userIdentifier: String, limit: Int): Observable<List<TransactionEntity>> {

        val localTransactions = localDataSource.getUserTransactions(userIdentifier, limit)
            .map { transactions ->
                transactions.map { transactionMapper.from(it) }
            }

        return remoteDataSource.getUserTransactions(userIdentifier, limit)
            .map { transactions ->
                localDataSource.saveUserTransactions(userIdentifier, transactions)
                transactions.map { transactionMapper.from(it) }
            }.onErrorResumeNext(Observable.empty())
            .concatWith(localTransactions)
    }

    override fun getFilteredTransactions(
        userIdentifier: String,
        credit: Boolean,
        debit: Boolean,
        flagged: Boolean
    ): Observable<List<TransactionEntity>> {
        return localDataSource.getFilteredTransactions(
            userIdentifier = userIdentifier,
            credit = credit,
            debit = debit,
            flagged = flagged
        ).map { transactions ->
            transactions.map { transactionMapper.from(it) }
        }
    }

    override fun updateTransaction(transaction: TransactionEntity): Completable {
        return localDataSource.updateTransaction(
            transactionMapper.to(transaction)
        )
    }
}