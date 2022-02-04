package org.drulabs.bankbuddy.utils

import com.google.gson.Gson
import io.reactivex.Observable
import org.drulabs.bankbuddy.remote.api.BankingService
import org.drulabs.bankbuddy.remote.model.ResponseWrapper

class FakeBankingService : BankingService {
    override fun getUserInformation(
        userIdentifier: String
    ): Observable<ResponseWrapper> {
        val responseWrapper = Gson()
            .fromJson(testData, ResponseWrapper::class.java)
        return Observable.just(responseWrapper)
    }
}