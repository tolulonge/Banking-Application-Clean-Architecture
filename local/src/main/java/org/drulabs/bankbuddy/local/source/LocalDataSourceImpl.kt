package org.drulabs.bankbuddy.local.source

import io.reactivex.Completable
import io.reactivex.Observable
import org.drulabs.bankbuddy.data.model.TransactionData
import org.drulabs.bankbuddy.data.model.UserInfoData
import org.drulabs.bankbuddy.data.repository.LocalDataSource
import org.drulabs.bankbuddy.local.database.TransactionDAO
import org.drulabs.bankbuddy.local.database.UserInfoDAO
import org.drulabs.bankbuddy.local.mapper.TransactionDataLocalMapper
import org.drulabs.bankbuddy.local.mapper.UserInfoDataLocalMapper
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val userInfoMapper: UserInfoDataLocalMapper,
    private val transactionMapper: TransactionDataLocalMapper,
    private val userInfoDAO: UserInfoDAO,
    private val transactionDAO: TransactionDAO
) : LocalDataSource {

    companion object {
        private const val DEFAULT_LIMIT = 40
        private const val TYPE_CREDIT = "credit"
        private const val TYPE_DEBIT = "debit"
    }

    override fun getUserInfo(identifier: String): Observable<UserInfoData> {
        return userInfoDAO.getUserInfo(identifier)
            .map {
                userInfoMapper.from(it)
            }
    }

    override fun saveUserInfo(userInfoData: UserInfoData) {
        userInfoDAO.addUserInfo(
            userInfoMapper.to(userInfoData)
        )
    }

    override fun getUserTransactions(userIdentifier: String, limit: Int): Observable<List<TransactionData>> {
        return transactionDAO.getUserTransactions(limit)
            .map { localTransactions ->
                localTransactions.map {
                    println("Local Invoked")
                    transactionMapper.from(it)
                }
            }
    }

    override fun saveUserTransactions(userIdentifier: String, transactions: List<TransactionData>) {
        transactionDAO.addTransactions(
            transactions.map {
                transactionMapper.to(it)
            }
        )
    }

    override fun getTransaction(transactionId: String): Observable<TransactionData> {
        return transactionDAO.getTransactionById(transactionId)
            .map {
                println("Local get transactions Invoked")
                transactionMapper.from(it)
            }
    }

    override fun getFilteredTransactions(
        userIdentifier: String,
        credit: Boolean?,
        debit: Boolean?,
        flagged: Boolean?
    ): Observable<List<TransactionData>> {
        return transactionDAO.getUserTransactions(DEFAULT_LIMIT)
            .map { localTransactions ->
                localTransactions
                    .filter { if (flagged == null || !flagged) true else (it.flagged == flagged) }
                    .filter { if (credit == null || !credit) true else (it.type == TYPE_CREDIT) }
                    .filter { if (debit == null || !debit) true else (it.type == TYPE_DEBIT) }
            }.map { filteredTransactions ->
                filteredTransactions.map {
                    transactionMapper.from(it)
                }
            }
    }

    override fun updateTransaction(transaction: TransactionData): Completable {
        return transactionDAO.updateTransaction(
            transactionMapper.to(transaction)
        )
    }
}
