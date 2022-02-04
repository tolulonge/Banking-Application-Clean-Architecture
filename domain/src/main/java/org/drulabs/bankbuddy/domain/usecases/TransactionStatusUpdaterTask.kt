package org.drulabs.bankbuddy.domain.usecases

import io.reactivex.Completable
import io.reactivex.Scheduler
import org.drulabs.bankbuddy.domain.entities.TransactionEntity
import org.drulabs.bankbuddy.domain.qualifiers.Background
import org.drulabs.bankbuddy.domain.qualifiers.Foreground
import org.drulabs.bankbuddy.domain.repository.BankingRepository
import org.drulabs.bankbuddy.domain.usecases.base.CompletableUseCase
import javax.inject.Inject

class TransactionStatusUpdaterTask @Inject constructor(
    private val bankingRepository: BankingRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : CompletableUseCase<TransactionEntity>(
    backgroundScheduler,
    foregroundScheduler
) {

    override fun generateCompletable(input: TransactionEntity?): Completable {
        if (input == null) {
            throw IllegalArgumentException("TransactionStatusUpdaterTask parameter can't be null")
        }
        return bankingRepository.updateTransaction(input)
    }
}