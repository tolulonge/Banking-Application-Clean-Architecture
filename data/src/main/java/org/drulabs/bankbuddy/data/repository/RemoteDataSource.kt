package org.drulabs.bankbuddy.data.repository

import io.reactivex.Observable
import org.drulabs.bankbuddy.data.model.TransactionData
import org.drulabs.bankbuddy.data.model.UserInfoData

interface RemoteDataSource {

    fun getUserInfo(identifier: String): Observable<UserInfoData>

    fun getUserTransactions(userIdentifier: String, limit: Int): Observable<List<TransactionData>>
}