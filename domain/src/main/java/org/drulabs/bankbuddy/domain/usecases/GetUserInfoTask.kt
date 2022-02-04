package org.drulabs.bankbuddy.domain.usecases

import io.reactivex.Observable
import io.reactivex.Scheduler
import org.drulabs.bankbuddy.domain.entities.UserInfoEntity
import org.drulabs.bankbuddy.domain.qualifiers.Background
import org.drulabs.bankbuddy.domain.qualifiers.Foreground
import org.drulabs.bankbuddy.domain.repository.BankingRepository
import org.drulabs.bankbuddy.domain.usecases.base.ObservableUseCase
import javax.inject.Inject

class GetUserInfoTask @Inject constructor(
    private val bankingRepository: BankingRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : ObservableUseCase<UserInfoEntity, String>(backgroundScheduler, foregroundScheduler) {

    override fun generateObservable(input: String?): Observable<UserInfoEntity> {
        if (input == null) {
            throw IllegalArgumentException("User identifier can't be null")
        }
        return bankingRepository.getUserInfo(input)
    }

}