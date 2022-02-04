package org.drulabs.bankbuddy.remote.api

import org.drulabs.bankbuddy.remote.model.ResponseWrapper
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface BankingService {

    @GET("assets/bankassist/{identifier}.json")
    fun getUserInformation(@Path("identifier") userIdentifier: String):
            Observable<ResponseWrapper>
}