package org.drulabs.bankbuddy.presentation.mapper

import org.drulabs.bankbuddy.domain.entities.UserInfoEntity
import org.drulabs.bankbuddy.presentation.model.UserInfo
import javax.inject.Inject

class UserInfoEntityMapper @Inject constructor(): Mapper<UserInfoEntity, UserInfo> {
    override fun from(e: UserInfo): UserInfoEntity {
        return UserInfoEntity(
            accountNumber = e.accountNumber,
            displayName = e.displayName,
            address = e.address,
            displayableJoinDate = e.displayableJoinDate,
            premiumCustomer = e.premiumCustomer,
            accountBalance = e.accountBalance,
            accountType = e.accountType,
            unbilledTransactionCount = e.unbilledTransactionCount
        )
    }

    override fun to(t: UserInfoEntity): UserInfo {
        return UserInfo(
            accountNumber = t.accountNumber,
            displayName = t.displayName,
            address = t.address,
            displayableJoinDate = t.displayableJoinDate,
            premiumCustomer = t.premiumCustomer,
            accountBalance = t.accountBalance,
            accountType = t.accountType,
            unbilledTransactionCount = t.unbilledTransactionCount,
            isEligibleForUpgrade = t.isEligibleForUpgrade
        )
    }
}