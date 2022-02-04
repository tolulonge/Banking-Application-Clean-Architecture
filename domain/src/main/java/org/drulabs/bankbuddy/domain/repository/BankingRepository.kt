package org.drulabs.bankbuddy.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import org.drulabs.bankbuddy.domain.entities.TransactionEntity
import org.drulabs.bankbuddy.domain.entities.UserInfoEntity

interface BankingRepository {

    fun getUserInfo(identifier: String): Observable<UserInfoEntity>

    fun getUserTransactions(userIdentifier: String, limit: Int): Observable<List<TransactionEntity>>

    fun getFilteredTransactions(
        userIdentifier: String,
        credit: Boolean,
        debit: Boolean,
        flagged: Boolean
    ): Observable<List<TransactionEntity>>

    fun updateTransaction(transaction: TransactionEntity): Completable
}
