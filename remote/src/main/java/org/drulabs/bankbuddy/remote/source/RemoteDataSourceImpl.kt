package org.drulabs.bankbuddy.remote.source

import io.reactivex.Observable
import org.drulabs.bankbuddy.data.model.TransactionData
import org.drulabs.bankbuddy.data.model.UserInfoData
import org.drulabs.bankbuddy.data.repository.RemoteDataSource
import org.drulabs.bankbuddy.remote.api.BankingService
import org.drulabs.bankbuddy.remote.mapper.Mapper
import org.drulabs.bankbuddy.remote.model.TransactionNetwork
import org.drulabs.bankbuddy.remote.model.UserInfoNetwork
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val userInfoMapper: Mapper<UserInfoData, UserInfoNetwork>,
    private val transactionMapper: Mapper<TransactionData, TransactionNetwork>,
    private val bankingService: BankingService
) : RemoteDataSource {

    override fun getUserInfo(identifier: String): Observable<UserInfoData> {
        return bankingService.getUserInformation(identifier)
            .map { response ->
                println("Remote Invoked")
                userInfoMapper.from(response.userInfo)
            }
    }

    override fun getUserTransactions(userIdentifier: String, limit: Int):
            Observable<List<TransactionData>> {
        return bankingService.getUserInformation(userIdentifier)
            .map { response ->
                println("Remote get transactions Invoked")
                response.transactions.map { transaction: TransactionNetwork ->
                    transactionMapper.from(transaction)
                }
            }
    }
}