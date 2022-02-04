package org.drulabs.bankbuddy.domain.usecases

import io.reactivex.Observable
import io.reactivex.Scheduler
import org.drulabs.bankbuddy.domain.entities.TransactionEntity
import org.drulabs.bankbuddy.domain.qualifiers.Background
import org.drulabs.bankbuddy.domain.qualifiers.Foreground
import org.drulabs.bankbuddy.domain.repository.BankingRepository
import org.drulabs.bankbuddy.domain.usecases.base.ObservableUseCase
import javax.inject.Inject

class GetUserTransactionsTask @Inject constructor(
    private val bankingRepository: BankingRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : ObservableUseCase<List<TransactionEntity>, GetUserTransactionsTask.Params>(
    backgroundScheduler,
    foregroundScheduler
) {
    override fun generateObservable(input: Params?): Observable<List<TransactionEntity>> {
        if (input == null) {
            throw IllegalArgumentException("GetUserTransactionsTask parameter can't be null")
        }
        return bankingRepository.getUserTransactions(input.identifier, input.limit)
    }

    data class Params(val identifier: String, val limit: Int)
}